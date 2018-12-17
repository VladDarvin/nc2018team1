package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class DeleteSequenceBuilder extends SequenceBuilder {
    private Mutable mutable;
    private BigInteger objectId;

    DeleteSequenceBuilder(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public Mutable build(Mutable mutable) {
        this.mutable = mutable;
        objectId = mutable.getObjectId();
        deleteObjReferences();
        deleteAttributes();
        deleteObject();
        return mutable;
    }

    protected void logSQLError(SQLException e, String inTable) {
        logSQLError(e, inTable, "Deletion");
    }

    void deleteObject() {
        try {
            PreparedStatement query = connection.prepareStatement(
                    "DELETE FROM OBJECTS WHERE OBJECT_ID = ?");
            query.setObject(1, objectId);
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
                "DELETE FROM ATTRIBUTES WHERE OBJECT_ID = ?")) {
            query.setObject(1, objectId);
            query.executeUpdate();
        }
        catch (SQLException e){
            logSQLError(e, "Attributes");
        }
    }

    private void deleteObjReferences() {
        Map<BigInteger, BigInteger> references = mutable.getReferences();

        if (noSuchElementsInObject(references)) return;

        try (PreparedStatement query = connection.prepareStatement(
                "DELETE FROM OBJREFERENCE WHERE OBJECT_ID = ?")) {
            query.setObject(1, objectId);
            query.executeUpdate();
        } catch (SQLException e) {
            logSQLError(e, "References");
        }
    }
}
