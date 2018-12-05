package com.nc.airport.backend.eav.dao;

import com.nc.airport.backend.eav.filtering.PagingDescriptor;
import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by User on 30.11.2018.
 */
class LazyDBFetcher {
    private Logger logger = LogManager.getLogger(LazyDBFetcher.class.getSimpleName());
    private Connection connection;

    LazyDBFetcher(Connection connection) {
        this.connection = connection;
    }

    /*
        SELECT * FROM
          (
            SELECT a.*, rownum rnum FROM
            (
              WITH OBJECTS_FAMILY AS
              (
                SELECT OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION, CONNECT_BY_ROOT OBJECT_ID BRANCH_OF
                FROM OBJECTS
                START WITH OBJECT_TYPE_ID = 8
                CONNECT BY OBJECTS.OBJECT_ID = PRIOR PARENT_ID
              )
              SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ATRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE
                FROM OBJECTS_FAMILY O
              JOIN ATTRTYPE ATRT
                  ON ATRT.OBJECT_TYPE_ID = O.OBJECT_TYPE_ID
              LEFT JOIN ATTRIBUTES A
                  ON ATRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID
              LEFT JOIN OBJREFERENCE R
                  ON ATRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID
              WHERE ATRT.ATTR_ID IN (45, 50, 55)
              ORDER BY BRANCH_OF, A.ATTR_ID
            ) a
          WHERE rownum <= 6)
        WHERE rnum >= 1;
     */
    private StringBuilder createSQLQuery(String startWith, String whereClause) {
        return new StringBuilder
                ("WITH OBJECTS_FAMILY AS ")
        .append("(SELECT OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION, CONNECT_BY_ROOT OBJECT_ID BRANCH_OF")
        .append(" FROM OBJECTS")
        .append(" START WITH ").append(startWith)
        .append(" CONNECT BY OBJECTS.OBJECT_ID = PRIOR PARENT_ID) ")

        .append("SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ")
            .append("ATRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE ")
        .append("FROM OBJECTS_FAMILY O ")
        .append("JOIN ATTRTYPE ATRT ")
            .append("ON ATRT.OBJECT_TYPE_ID = O.OBJECT_TYPE_ID ")
        .append("LEFT JOIN ATTRIBUTES A ")
            .append("ON ATRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID ")
        .append("LEFT JOIN OBJREFERENCE R ")
            .append("ON ATRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID ")
        .append(whereClause)
        .append("ORDER BY BRANCH_OF, A.ATTR_ID");
    }

    Mutable getMutable(BigInteger objectId, Collection<BigInteger> attributesId) throws SQLException {
        Mutable mutable = new Mutable();
        PreparedStatement statement = null;
        ResultSet result = null;

        String fullQuery = createSQLQuery("OBJECT_ID = ?", transferAttributesId(attributesId.size()))
                .toString();
        logger.log(Level.INFO, "Executing sequence:\n"+fullQuery);
        try {
            statement = connection.prepareStatement(fullQuery);
            result = resultSingleMutable(objectId, attributesId, statement);
            pullAttributesForSingleObj(result, mutable, Collections.max(attributesId));
            pullGeneralInfo(result, mutable);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Could not get mutable " + objectId);
            throw e;
        } finally {
            if (result != null)
                result.close();
            else logger.log(Level.ERROR, "Could not define ResultSet");

            if (statement != null)
                statement.close();
            else logger.log(Level.ERROR, "Could not define Statement");
        }

        return mutable;
    }

    List<Mutable> getMutables(BigInteger objType, Collection<BigInteger> attributesId,
                              int pagingFrom, int pagingTo) throws SQLException {
        List<Mutable> mutables = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();

        StringBuilder basicQuery = createSQLQuery("OBJECT_TYPE_ID = ?",
                                                    transferAttributesId(attributesId.size()));

        String fullQuery = paging.getPaging(basicQuery,
                                            getObjectivePage(pagingFrom, attributesId.size()),
                                            getObjectivePage(pagingTo + 1, attributesId.size()) - 1);
        try {
            statement = connection.prepareStatement(fullQuery);
            logger.log(Level.INFO, "Executing sequence:\n"+fullQuery);
            result = resultMultipleMutables(objType, attributesId, statement);
            for (int i = 1; i <= pagingTo - pagingFrom; i++){
                Mutable mutable = new Mutable();
                pullAttributes(result, mutable, Collections.max(attributesId));
                pullGeneralInfo(result, mutable);
                mutables.add(mutable);
            }
        } finally {
            if (result != null)
                result.close();

            if (statement != null)
                statement.close();
        }
        return mutables;
    }

    List<Mutable> getMutables(Collection<BigInteger> objectsId,
                              Collection<BigInteger> attributesId) throws SQLException {
        List<Mutable> mutables = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet result = null;

        String fullQuery = createSQLQuery(transferObjectsId(objectsId.size()),
                transferAttributesId(attributesId.size())).toString();

        try {
            statement = connection.prepareStatement(fullQuery);
            logger.log(Level.INFO, "Executing sequence:\n"+fullQuery);
            result = resultMultipleMutables(objectsId, attributesId, statement);
            for (int i = 1; i <= objectsId.size(); i++){
                Mutable mutable = new Mutable();
                pullAttributes(result, mutable, Collections.max(attributesId));
                pullGeneralInfo(result, mutable);
                mutables.add(mutable);
            }
        } finally {
            if (result != null)
                result.close();

            if (statement != null)
                statement.close();
        }
        return mutables;
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
        StringBuilder attrSet = new StringBuilder("WHERE ATRT.ATTR_ID IN")
                .append(transferElements(amount));
        return attrSet.toString();
    }

    private String transferObjectsId(int amount) {
        StringBuilder attrSet = new StringBuilder(" OBJECT_ID IN")
                .append(transferElements(amount));
        return attrSet.toString();
    }

    private StringBuilder transferElements(int amount) {
        StringBuilder attrSet = new StringBuilder(" (");

        for (int i = 1; i < amount; i++)
            attrSet.append("?, ");
        attrSet.append("?) ");
        return attrSet;
    }

    private void pullGeneralInfo(ResultSet result, Mutable mutable) throws SQLException {
        mutable.setObjectId(applyBigInt(1, result));
        mutable.setParentId(applyBigInt(2, result));
        mutable.setObjectTypeId(applyBigInt(3, result));
        mutable.setObjectName(result.getString(4));
        mutable.setObjectDescription(result.getString(5));
    }

    private void pullAttributes(ResultSet result, Mutable mutable, BigInteger lastAttrId) throws SQLException {
        Map<BigInteger, String> values = new LinkedHashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> listValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> references = new LinkedHashMap<>();

        while (result.next()) {
            pullAttr(result, values, references, listValues, dateValues);
            if (result.getString(6).equals(lastAttrId.toString()))
                break;
        }

        setMutableAttributes(mutable, values, dateValues, listValues, references);
    }

    private void pullAttributesForSingleObj(ResultSet result, Mutable mutable, BigInteger lastAttrId) throws SQLException {
        Map<BigInteger, String> values = new LinkedHashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> listValues = new LinkedHashMap<>();
        Map<BigInteger, BigInteger> references = new LinkedHashMap<>();

        while (result.next()) {
            pullAttr(result, values, references, listValues, dateValues);
            if (result.getString(6).equals(lastAttrId.toString()))
                break;
        }

        setMutableAttributes(mutable, values, dateValues, listValues, references);
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
                          Map<BigInteger, LocalDateTime> dateValues) throws SQLException{

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

    private ResultSet resultSingleMutable(BigInteger objectId,
                                          Collection<BigInteger> attributesId,
                                          PreparedStatement statement) throws SQLException {

        statement.setObject(1, objectId);
        setAttributes(statement, attributesId, 1);
        return statement.executeQuery();
    }

    private ResultSet resultMultipleMutables(BigInteger objType,
                                             Collection<BigInteger> attributesId,
                                             PreparedStatement statement) throws SQLException {

        statement.setObject(1, objType);
        setAttributes(statement, attributesId, 1);
        return statement.executeQuery();
    }

    private ResultSet resultMultipleMutables(Collection<BigInteger> objectsId,
                                             Collection<BigInteger> attributesId,
                                             PreparedStatement statement) throws SQLException {
        setObjects(statement, objectsId);
        setAttributes(statement, attributesId, objectsId.size());
        return statement.executeQuery();
    }

    private void setAttributes(PreparedStatement statement,
                               Collection<BigInteger> attributesId,
                               int indexesBefore) throws SQLException {

        setElements(statement, attributesId, indexesBefore);
    }

    private void setObjects(PreparedStatement statement,
                            Collection<BigInteger> objectsId) throws SQLException {
        setElements(statement, objectsId, 0);
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
