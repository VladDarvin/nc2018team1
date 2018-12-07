package com.nc.airport.backend.eav.dao;

import com.nc.airport.backend.eav.mutable.Mutable;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class DeleteSequenceBuilder extends SequenceBuilder {
    private Mutable mutable;
    private Connection connection;
    private BigInteger objectId;

    DeleteSequenceBuilder(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void build(Mutable mutable) {
        this.mutable = mutable;
        objectId = mutable.getObjectId();
        deleteObjReferences();
        deleteAttributes();
        deleteObject();
    }

    protected void logSQLError(SQLException e, String inTable) {
        logSQLError(e, inTable, "Deletion");
    }

    void deleteObject() {
        try {
            PreparedStatement query = connection.prepareStatement(
                    "DELETE FROM OBJECTS WHERE OBJECT_ID = ?");
            query.setObject(1, mutable.getObjectId());
            query.executeUpdate();
        } catch (SQLException e) {
            logSQLError(e, "Objects");
        }
    }

    private void deleteAttributes() {
        deleteValues(mutable.getValues());
        deleteValues(mutable.getDateValues());
        deleteValues(mutable.getListValues());
    }

    private void deleteValues(Map<BigInteger, ?> values) {
        if (noSuchElementsInObject(values)) return;

        try (PreparedStatement query = connection.prepareStatement(
                "DELETE FROM ATTRIBUTES WHERE ATTR_ID = ? AND OBJECT_ID = " + objectId)) {
            for (Map.Entry<BigInteger, ?> entry : values.entrySet()) {
                query.setObject(1, entry.getKey());
                query.addBatch();
            }
            query.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "Attributes");
        }
    }

    private void deleteObjReferences() {
        Map<BigInteger, BigInteger> references = mutable.getReferences();

        if (noSuchElementsInObject(references)) return;

        try (PreparedStatement query = connection.prepareStatement(
                "DELETE FROM OBJREFERENCE WHERE ATTR_ID = ? AND OBJECT_ID = ? AND REFERENCE = ?")) {
            for (Map.Entry<BigInteger, BigInteger> entry : references.entrySet()) {
                query.setObject(1, entry.getKey());
                query.setObject(2, objectId);
                query.setObject(3, entry.getValue());
                query.addBatch();
            }
            query.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "References");
        }
    }
}
