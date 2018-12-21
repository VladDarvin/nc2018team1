package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging.PagingDescriptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

class TallLazyDBFetcher {
    private Logger logger = LogManager.getLogger(TallLazyDBFetcher.class.getSimpleName());
    private Connection connection;

    TallLazyDBFetcher(Connection connection) {
        this.connection = connection;
    }

    /*
        SELECT * FROM
          (
            SELECT a.*, rownum rnum FROM
            (
              WITH OBJECT_ATTRIBUTES AS
            (
              SELECT ATTR_ID
              FROM ATTRTYPE ATTRT
              JOIN OBJTYPE OBJT
                ON ATTRT.OBJECT_TYPE_ID = OBJT.OBJECT_TYPE_ID
              START WITH OBJT.OBJECT_TYPE_ID = 8
              CONNECT BY OBJT.OBJECT_TYPE_ID = PRIOR PARENT_ID
              GROUP BY ATTR_ID
            )
            SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ATTRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE
              FROM OBJECTS O
            JOIN OBJECT_ATTRIBUTES ATTRT
                ON O.OBJECT_TYPE_ID = 8
            LEFT JOIN ATTRIBUTES A
                ON ATTRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID
            LEFT JOIN OBJREFERENCE R
                ON ATTRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID
            WHERE O.OBJECT_ID = 21
                AND ATTRT.ATTR_ID IN (45, 50, 55, 43, 48)
            ORDER BY O.OBJECT_ID, A.ATTR_ID
            ) a
          WHERE rownum <= 6)
        WHERE rnum >= 1;
     */
    private StringBuilder createSQLQuery(String objectTypeId, String whereClause) {
        return new StringBuilder
                ("WITH OBJECT_ATTRIBUTES AS ")
                .append("(SELECT ATTR_ID")
                .append(" FROM ATTRTYPE ATTRT")
                .append(" JOIN OBJTYPE OBJT")
                .append("   ON ATTRT.OBJECT_TYPE_ID = OBJT.OBJECT_TYPE_ID")
                .append(" START WITH OBJT.OBJECT_TYPE_ID = ").append(objectTypeId)
                .append(" CONNECT BY OBJT.OBJECT_TYPE_ID = PRIOR PARENT_ID ")
                .append(" GROUP BY ATTR_ID) ")

                .append("SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ")
                .append("ATTRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE ")
                .append("FROM OBJECTS O ")
                .append(" JOIN OBJECT_ATTRIBUTES ATTRT ")
                .append("  ON O.OBJECT_TYPE_ID = ").append(objectTypeId)
                .append(" LEFT JOIN ATTRIBUTES A ")
                .append("  ON ATTRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID ")
                .append(" LEFT JOIN OBJREFERENCE R ")
                .append("  ON ATTRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID ")
                .append(whereClause)
                .append("ORDER BY O.OBJECT_ID, A.ATTR_ID");
    }

    Mutable getMutable(BigInteger objectId, Collection<BigInteger> attributesId) {
        Mutable mutable = new Mutable();
        PreparedStatement statement = null;
        ResultSet result = null;

        String fullQuery = createSQLQuery(getObjTypeIdInQuery(objectId.toString()),
                "WHERE O.OBJECT_ID = " + objectId + " AND " + transferAttributesId(attributesId.size()))
                .toString();
        logger.log(Level.INFO, "Executing sequence:\n" + fullQuery);
        try {
            statement = connection.prepareStatement(fullQuery);
            result = resultSingleMutable(attributesId, statement);
            result.next();
            pullGeneralInfo(result, mutable);
            pullAttributes(result, mutable);
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
            closeResultSetAndStatement(result, statement);
        }

        return mutable;
    }

    List<Mutable> getMutables(BigInteger objType, Collection<BigInteger> attributesId,
                              int pagingFrom, int pagingTo) {
        List<Mutable> mutables = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();

        StringBuilder basicQuery = createSQLQuery(objType.toString(),
                "WHERE "+transferAttributesId(attributesId.size()));

        String fullQuery = paging.getPaging(basicQuery,
                getObjectivePage(pagingFrom, attributesId.size()),
                getObjectivePage(pagingTo + 1, attributesId.size()) - 1);
        try {
            logger.log(Level.INFO, "Executing sequence:\n" + fullQuery);
            statement = connection.prepareStatement(fullQuery);
            result = resultMultipleMutables(attributesId, statement);
            while (result.next()){
                Mutable mutable = new Mutable();
                pullAttributes(result, mutable, attributesId.size());
                pullGeneralInfo(result, mutable);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    List<Mutable> getMutables(List<BigInteger> objectsId,
                              Collection<BigInteger> attributesId) {
        List<Mutable> mutables = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet result = null;

        String fullQuery = createSQLQuery(getObjTypeIdInQuery(objectsId.get(0).toString()),
                "WHERE " + transferObjectsId(objectsId.size()) +
                        " AND " + transferAttributesId(attributesId.size()))
                        .toString();

        try {
            statement = connection.prepareStatement(fullQuery);
            logger.log(Level.INFO, "Executing sequence:\n" + fullQuery);
            result = resultMultipleMutables(objectsId, attributesId, statement);
            while (result.next()){
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    private void closeResultSetAndStatement(ResultSet result, Statement statement) {
        if (result != null)
            try {
                result.close();
            } catch (SQLException e) {
                logger.error(e);
                throw new DatabaseConnectionException("Could not close result set", e);
            }

        if (statement != null)
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error(e);
                throw new DatabaseConnectionException("Could not close statement", e);
            }
    }

    /*
    For each object lazy statement has multiple rows each for one attribute or reference
    thus, if pageFrom - pageTo represents number of objects retrieved, we have to convert
    dull number of object into number of row from which this object starts
     */
    private static int getObjectivePage(int dullPage, int rowsPerObject) {
        return 1 + rowsPerObject * (dullPage - 1);  //formula for the Nth term of an arithmetic progression
    }

    private String transferAttributesId(int amount) {
        return " ATTRT.ATTR_ID IN" + transferElements(amount);
    }

    private String transferObjectsId(int amount) {
        return " O.OBJECT_ID IN" + transferElements(amount);
    }

    private String getObjTypeIdInQuery(String objectId) {
        return "(SELECT OBJECT_TYPE_ID FROM OBJECTS WHERE OBJECT_ID = " + objectId + ") ";
    }

    private StringBuilder transferElements(int amount) {
        StringBuilder attrSet = new StringBuilder(" (");

        for (int i = 1; i < amount; i++)
            attrSet.append("?, ");
        attrSet.append("?) ");
        return attrSet;
    }

    private void pullGeneralInfo(ResultSet result, Mutable mutable) {
        try {
            mutable.setObjectId(applyBigInt(1, result));
            mutable.setParentId(applyBigInt(2, result));
            mutable.setObjectTypeId(applyBigInt(3, result));
            mutable.setObjectName(result.getString(4));
            mutable.setObjectDescription(result.getString(5));
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Failed to pull OBJECTS table data", e);
        }
    }

    private void pullAttributes(ResultSet result, Mutable mutable, int objectAttributesAmount) {
        Map<BigInteger, String> values = new LinkedHashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> listValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> references = new LinkedHashMap<>();

        try {
            BigInteger startRow = applyBigInt(11, result);
            do {
                pullAttr(result, values, references, listValues, dateValues);
                if (isLastAttrOfObject(startRow, applyBigInt(11, result), objectAttributesAmount)) {
                    break;
                }
            } while (result.next());
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Failed pulling attributes from result set", e);
        }

        setMutableAttributes(mutable, values, dateValues, listValues, references);
    }

    private void pullAttributes(ResultSet result,
                                Mutable mutable) {
        Map<BigInteger, String> values = new LinkedHashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> listValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> references = new LinkedHashMap<>();

        try {
            do {
                pullAttr(result, values, references, listValues, dateValues);
            } while (result.next());
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Failed pulling attributes from result set", e);
        }

        setMutableAttributes(mutable, values, dateValues, listValues, references);
    }

    private boolean isLastAttrOfObject(BigInteger startRow, BigInteger currentRow, int objAttrSize) {
        return (currentRow.subtract(startRow).add(BigInteger.ONE).equals(BigInteger.valueOf(objAttrSize)));
    }

    private void setMutableAttributes(Mutable mutable,
                                      Map<BigInteger, String> values,
                                      Map<BigInteger, LocalDateTime> dateValues,
                                      Map<BigInteger, BigInteger> listValues,
                                      Map<BigInteger, BigInteger> references) {

        mutable.setValues(values);
        mutable.setDateValues(dateValues);
        mutable.setListValues(listValues);
        mutable.setReferences(references);
    }

    private void pullAttr(ResultSet result,
                          Map<BigInteger, String> values,
                          Map<BigInteger, BigInteger> references,
                          Map<BigInteger, BigInteger> listValues,
                          Map<BigInteger, LocalDateTime> dateValues) throws SQLException {

        String value = result.getString(7);
        if (value != null)
            values.put(applyBigInt(6, result), value);
        else {
            BigInteger reference = applyBigInt(10, result);
            if (reference != null)
                references.put(applyBigInt(6, result), reference);
            else {
                BigInteger listValue = applyBigInt(9, result);
                if (listValue != null)
                    listValues.put(applyBigInt(6, result), listValue);
                else dateValues.put(applyBigInt(6, result), result.getTimestamp(8).toLocalDateTime());
            }
        }
    }

    private BigInteger applyBigInt(String bigInt) {
        return bigInt == null ? null : new BigInteger(bigInt);
    }

    private BigInteger applyBigInt(int columnNumber, ResultSet result) throws SQLException {
        return applyBigInt(result.getString(columnNumber));
    }

    private ResultSet resultSingleMutable(Collection<BigInteger> attributesId,
                                          PreparedStatement statement) {

        setAttributes(statement, attributesId, 0);
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
            throw new BadDBRequestException("Error occurred after query execution", e);
        }
    }

    private ResultSet resultMultipleMutables(Collection<BigInteger> attributesId,
                                             PreparedStatement statement) {

        setAttributes(statement, attributesId, 0);
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
            throw new BadDBRequestException("Error is found after query execution", e);
        }
    }

    private ResultSet resultMultipleMutables(Collection<BigInteger> objectsId,
                                             Collection<BigInteger> attributesId,
                                             PreparedStatement statement) {
        setObjects(statement, objectsId);
        setAttributes(statement, attributesId, objectsId.size());
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
            throw new BadDBRequestException("Error is found after query execution", e);
        }
    }

    private void setAttributes(PreparedStatement statement,
                               Collection<BigInteger> attributesId,
                               int indexesBefore) {

        try {
            setElements(statement, attributesId, indexesBefore);
        } catch (SQLException e) {
            logger.error(e);
            throw new BadDBRequestException("Could not set given attributes", e);
        }
    }

    private void setObjects(PreparedStatement statement,
                            Collection<BigInteger> objectsId) {
        try {
            setElements(statement, objectsId, 0);
        } catch (SQLException e) {
            logger.error(e);
            throw new BadDBRequestException("Could not set given objects", e);
        }
    }

    private void setElements(PreparedStatement statement,
                             Collection<BigInteger> elementsId,
                             int indexesBefore) throws SQLException {

        Iterator<BigInteger> elementsIterator = elementsId.iterator();
        for (int i = indexesBefore + 1; i <= elementsId.size() + indexesBefore; i++) {
            BigInteger elementId = elementsIterator.next();
            statement.setObject(i, elementId);
        }
    }

}