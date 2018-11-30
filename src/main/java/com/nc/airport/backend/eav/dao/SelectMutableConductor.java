package com.nc.airport.backend.eav.dao;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 30.11.2018.
 */
class SelectMutableConductor {
    private Logger logger = LogManager.getLogger(SelectMutableConductor.class.getSimpleName());
    private Connection connection;

    SelectMutableConductor(Connection connection) {
        this.connection = connection;
    }

    /*
        SELECT A.OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION,
          A.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE
        FROM ATTRIBUTES A JOIN
          ( SELECT OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION
            FROM OBJECTS
            START WITH OBJECT_TYPE_ID = 8
            CONNECT BY OBJECTS.OBJECT_ID = PRIOR PARENT_ID
          ) O
          ON A.OBJECT_ID = O.OBJECT_ID
          LEFT JOIN OBJREFERENCE OBJR ON O.OBJECT_ID = OBJR.OBJECT_ID
        WHERE A.ATTR_ID IN (41, 42, 55, 51, 50)
     */
    private String createSQLQuery(String startsWith, String whereClause) {
        StringBuilder sql =
            new StringBuilder("SELECT A.OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION, ")
                        .append("A.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE ")
                        .append("FROM ATTRIBUTES A JOIN ")
                            .append("(SELECT OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION ")
                            .append("FROM OBJECTS ")
                            .append("START WITH ").append(startsWith)
                            .append(" CONNECT BY OBJECTS.OBJECT_ID = PRIOR PARENT_ID")
                            .append(") O ")
                        .append("ON A.OBJECT_ID = O.OBJECT_ID ")
                        .append("LEFT JOIN OBJREFERENCE OBJR ON O.OBJECT_ID = OBJR.OBJECT_ID ")
                        .append(whereClause);
        return sql.toString();
    }

    Mutable getMutable(BigInteger objectId, Collection<BigInteger> attributesId) throws SQLException {
        Mutable mutable = new Mutable();
        PreparedStatement statement = null;
        ResultSet result = null;

        String sql = createSQLQuery("OBJECT_ID = " + objectId, transferAttributesId(attributesId.size()));
        try {
            statement = connection.prepareStatement(sql);
            result = getResultSetForSingleMutable(objectId, attributesId, statement);
            pullGeneralInfo(result, mutable, objectId);
            pullAttributes(result, mutable);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Could not get mutable " + objectId);
            throw e;
        } finally {
            if (result != null)
                result.close();
            else logger.log(Level.WARN, "Could not define ResultSet");

            if (statement != null)
                statement.close();
            else logger.log(Level.WARN, "Could not define StatementSet");
        }

        return mutable;
    }

    private String transferAttributesId(int amount) {
        StringBuilder attrSet = new StringBuilder("WHERE A.ATTR_ID IN (");

        for (int i = 1; i < amount; i++)
            attrSet.append("?, ");
        attrSet.append("?) ");
        return attrSet.toString();
    }

    private void pullGeneralInfo(ResultSet result, Mutable mutable, BigInteger objectId) throws SQLException {
        result.next();
        mutable.setObjectId(objectId);
        String nextBigInt = result.getString(2);
        mutable.setParentId(applyBigInt(nextBigInt));
        nextBigInt = result.getString(3);
        mutable.setObjectTypeId(applyBigInt(nextBigInt));
        mutable.setObjectName(result.getString(4));
        mutable.setObjectDescription(result.getString(5));
    }

    private void pullAttributes(ResultSet result, Mutable mutable) throws SQLException {
        Map<BigInteger, String> values = new HashMap<>();
        Map<BigInteger, LocalDateTime> dateValues = new HashMap<>();
        Map<BigInteger, BigInteger> listValues = new HashMap<>();
        Map<BigInteger, BigInteger> references = new HashMap<>();

        pullAttr(result, values, references, listValues, dateValues);
        while (result.next()) {
            if (applyBigInt(1, result).equals(mutable.getObjectId()))
                pullAttr(result, values, references, listValues, dateValues);
            else {
                result.previous();
                break;
            }
        }

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

    private ResultSet getResultSetForSingleMutable(BigInteger objectId, Collection<BigInteger> attributesId, PreparedStatement statement)
            throws SQLException {

        try {
            Iterator<BigInteger> attrIterator = attributesId.iterator();

            for (int i = 1; i <= attributesId.size(); i++) {
                statement.setObject(i, attrIterator.next());
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to get mutable with objectId = " + objectId);
            throw e;
        }
    }
}
