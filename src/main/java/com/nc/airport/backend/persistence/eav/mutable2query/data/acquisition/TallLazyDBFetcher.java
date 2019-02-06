package com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition;

import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging.PagingDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TallLazyDBFetcher {
    private Logger logger = LogManager.getLogger(TallLazyDBFetcher.class.getSimpleName());
    private Connection connection;

    public TallLazyDBFetcher(Connection connection) {
        this.connection = connection;
    }

    public Mutable getMutable(BigInteger objectId, Collection<BigInteger> attributesId) {
        QueryCreator queryCreator = new QueryCreator();
        Mutable mutable = new Mutable();
//        PreparedStatement statement = null;
//        ResultSet result = null;

        String fullQuery = queryCreator.createTallLazyQuery(getObjTypeIdInQuery(objectId.toString()),
                "WHERE O.OBJECT_ID = " + objectId + " AND " + transferAttributesId(attributesId.size()))
                .toString();

        queryCreator.logSequence(logger, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultSingleMutable(attributesId, statement)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultSingleMutable(attributesId, statement);
            result.next();
            pullGeneralInfo(result, mutable);
            pullAttributes(result, mutable);
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }

        return mutable;
    }

    public List<Mutable> getMutables(BigInteger objType, Collection<BigInteger> attributesId,
                                     int pagingFrom, int pagingTo) {
        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();

        StringBuilder basicQuery = queryCreator.createTallLazyQuery(objType.toString(),
                "WHERE " + transferAttributesId(attributesId.size()));

        String fullQuery = paging.getPaging(basicQuery,
                getObjectivePage(pagingFrom, attributesId.size()),
                getObjectivePage(pagingTo + 1, attributesId.size()) - 1);

        queryCreator.logSequence(logger, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(attributesId, statement)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(attributesId, statement);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullAttributes(result, mutable, attributesId.size());
                pullGeneralInfo(result, mutable);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    public List<Mutable> getMutables(List<BigInteger> objectsId,
                                     Collection<BigInteger> attributesId) {
        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;

        String fullQuery = queryCreator.createTallLazyQuery(getObjTypeIdInQuery(objectsId.get(0).toString()),
                "WHERE " + transferObjectsId(objectsId.size()) +
                        " AND " + transferAttributesId(attributesId.size()))
                .toString();

        queryCreator.logSequence(logger, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(objectsId, attributesId, statement)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(objectsId, attributesId, statement);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, attributesId.size());
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
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
    For each object lazy statement has multiple rows each for one attribute or reference/
    Thus, if pageFrom - pageTo represents number of objects retrieved, we have to convert
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
            throw new BadDBRequestException("Database doesn't contain requested object", e);
        }
    }

    private void pullAttributes(ResultSet result, Mutable mutable, int objectAttributesAmount) {
        Map<BigInteger, String> values = new LinkedHashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> listValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> references = new LinkedHashMap<>();

        try {
            int attrIterator = 1;
            do {
                pullAttr(result, values, references, listValues, dateValues);
                attrIterator++;
                if (attrIterator > objectAttributesAmount)
                    break;
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

//    private boolean isLastAttrOfObject(BigInteger startRow, BigInteger currentRow, int objAttrSize) {
//        return (currentRow.subtract(startRow).add(BigInteger.ONE).equals(BigInteger.valueOf(objAttrSize)));
//    }

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
                else {
                    Timestamp dateValueTimeStamp = result.getTimestamp(8);
                    if (dateValueTimeStamp != null) {
                        LocalDateTime dateValue = dateValueTimeStamp.toLocalDateTime();
                        dateValues.put(applyBigInt(6, result), dateValue);
                    }
                }
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