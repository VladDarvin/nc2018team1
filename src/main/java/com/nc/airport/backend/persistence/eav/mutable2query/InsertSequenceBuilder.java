package com.nc.airport.backend.persistence.eav.mutable2query;


import com.nc.airport.backend.persistence.eav.Mutable;
import org.apache.logging.log4j.LogManager;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class InsertSequenceBuilder extends SequenceBuilder {
    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(InsertSequenceBuilder.class.getSimpleName());
    private Mutable mutable;
    private BigInteger objectId;

    InsertSequenceBuilder(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public Mutable build(Mutable mutable) {
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

    private void logSQLError(SQLException e, String inTable) {
        logSQLError(e, inTable, "insertion");
    }

    private void insertIntoObjects() {
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

    private void insertIntoAttributes() {
        insertValues(mutable.getValues(), "value");
        insertValues(mutable.getDateValues(), "date_value");
        insertValues(mutable.getListValues(), "list_value_id");
    }

    private void insertValues(Map<BigInteger, ?> values, String valueType) {
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

    private void insertIntoObjReferences() {
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
