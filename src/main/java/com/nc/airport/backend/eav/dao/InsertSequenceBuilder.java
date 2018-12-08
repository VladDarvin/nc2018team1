package com.nc.airport.backend.eav.dao;


import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.LogManager;

import java.math.BigInteger;
import java.sql.*;
import java.util.Map;

class InsertSequenceBuilder extends SequenceBuilder {
    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(InsertSequenceBuilder.class.getSimpleName());
    private Mutable mutable;
    private Connection connection;
    private BigInteger objectId;

    InsertSequenceBuilder(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Mutable build(Mutable mutable) throws SQLException {
        this.mutable = mutable;
        objectId = getNewObjectId();

        insertIntoObjects();
        insertIntoAttributes();
        insertIntoObjReferences();

        if (mutable.getObjectId() != null)
            logger.warn("Changed inserted mutable object_id from " + mutable.getObjectId() + " to " + objectId);
        mutable.setObjectId(objectId);
        return mutable;
    }

    private BigInteger getNewObjectId() throws SQLException {
        try {
            ResultSet nextVal = connection.createStatement()
                    .executeQuery("SELECT COALESCE(MIN(O1.OBJECT_ID+1), 1)\n" +
                            "  FROM OBJECTS O1 LEFT JOIN OBJECTS O2 ON O1.OBJECT_ID + 1 = O2.OBJECT_ID\n" +
                            "  WHERE O2.OBJECT_ID IS NULL;");
            nextVal.next();
            return new BigInteger(nextVal.getString(1));
        } catch (SQLException e) {
            logger.error("Failed to get new object id from sequence");
            throw e;
        }
    }

    private void logSQLError(SQLException e, String inTable) throws SQLException{
        logSQLError(e, inTable, "insertion");
    }

    private void insertIntoObjects() throws SQLException{
        try (PreparedStatement statement
                     = connection.prepareStatement(
                "INSERT INTO OBJECTS " +
                        "(OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (?, ?, ?, ?, ?)")) {

            statement.setObject(1, objectId);
            statement.setObject(2, mutable.getParentId());
            statement.setObject(3, mutable.getObjectTypeId());
            statement.setString(4, mutable.getObjectName());
            statement.setString(5, mutable.getObjectDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            logSQLError(e, "Objects");
        }
    }

    private void insertIntoAttributes() throws SQLException {
        insertValues(mutable.getValues(), "value");
        insertValues(mutable.getDateValues(), "date_value");
        insertValues(mutable.getListValues(), "list_value_id");
    }

    private void insertValues(Map<BigInteger, ?> values, String valueType) throws SQLException {
        if (noSuchElementsInObject(values)) return;

        String sql = "INSERT INTO ATTRIBUTES (attr_id, " + valueType + ", object_id) VALUES (?,?, " + objectId + ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<BigInteger, ?> entry : values.entrySet()) {
                statement.setObject(1, entry.getKey());
                statement.setObject(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "Attributes while operating " + valueType);
        }
    }

    private void insertIntoObjReferences() throws SQLException {
        Map<BigInteger, BigInteger> references = mutable.getReferences();
        if (noSuchElementsInObject(references)) return;

        String sql = "INSERT INTO OBJREFERENCE (ATTR_ID,OBJECT_ID, REFERENCE) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<BigInteger, BigInteger> entry : references.entrySet()) {
                statement.setObject(1, entry.getKey());
                statement.setObject(2, objectId);
                statement.setObject(3, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "References");
        }
    }
}
