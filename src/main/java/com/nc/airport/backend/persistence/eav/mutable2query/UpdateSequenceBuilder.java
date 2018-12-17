package com.nc.airport.backend.persistence.eav.mutable2query;


import com.nc.airport.backend.persistence.eav.Mutable;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class UpdateSequenceBuilder extends SequenceBuilder{
    private Mutable mutable;
    private BigInteger objectId;

    UpdateSequenceBuilder(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public Mutable build(Mutable mutable) {
        this.mutable = mutable;

        if (mutable.getObjectId() == null) {
            objectId = getNewObjectId();
            mutable.setObjectId(objectId);
        }
        else {
            objectId = mutable.getObjectId();
        }

        updateObject();
        updateAttributes();
        updateReferencesOfObjReferences();
        return mutable;
    }

    protected void logSQLError(SQLException e, String inTable) {
        logSQLError(e, inTable, "update");
    }

    /**
     * This method has a restriction to not change Object_ID or Object_Type_ID
     * for security purposes.
     */
    protected void updateObject() {
        StringBuilder sql = new StringBuilder("MERGE INTO OBJECTS O")
                .append(" USING (SELECT ? PARENT_ID, ? NAME, ? DESCRIPTION, ").append(objectId)
                .append(" OBJECT_ID FROM dual) NEW ON (O.OBJECT_ID = NEW.OBJECT_ID)")
                .append(" WHEN MATCHED THEN UPDATE SET O.PARENT_ID = NEW.PARENT_ID, O.NAME = NEW.NAME,")
                .append(" O.DESCRIPTION = NEW.DESCRIPTION")
                .append(" WHEN NOT MATCHED THEN INSERT")
                .append(" VALUES (NEW.OBJECT_ID, NEW.PARENT_ID, ?, NEW.NAME, NEW.DESCRIPTION)");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())){
            statement.setObject(1, mutable.getParentId());
            statement.setString(2, mutable.getObjectName());
            statement.setString(3, mutable.getObjectDescription());
            statement.setObject(4, mutable.getObjectTypeId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            logSQLError(e, "Objects");
        }
    }

    protected void updateAttributes() {
        updateValues(mutable.getValues(), "VALUE ");
        updateValues(mutable.getDateValues(), "DATE_VALUE ");
        updateValues(mutable.getListValues(), "LIST_VALUE_ID");
    }

    private void updateValues(Map<BigInteger, ?> values, String valueType) {
        if (noSuchElementsInObject(values)) return;
        StringBuilder sql =
        new StringBuilder("MERGE INTO ATTRIBUTES A ")
        .append("USING (SELECT ? new_value, ? ATTR_ID, ").append(objectId).append(" OBJECT_ID FROM dual) B ")
        .append("ON (A.ATTR_ID = B.ATTR_ID AND A.OBJECT_ID = B.OBJECT_ID) ")
        .append("WHEN MATCHED THEN UPDATE SET A.").append(valueType).append(" = B.new_value ")
        .append("WHEN NOT MATCHED THEN INSERT (ATTR_ID, OBJECT_ID, ").append(valueType).append(") ")
        .append("VALUES (B.ATTR_ID, B.OBJECT_ID, B.new_value)");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (Map.Entry<BigInteger, ?> entry : values.entrySet()) {
                statement.setObject(1, entry.getValue());
                statement.setObject(2, entry.getKey());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "Attributes while operating " + valueType);
        }
    }


    /**
     * Can only update the REFERENCE value, can not change attr_id and object_id
     */
    private void updateReferencesOfObjReferences() {
        Map<BigInteger, BigInteger> references = mutable.getReferences();
        if (noSuchElementsInObject(references)) return;

        StringBuilder sql = new StringBuilder("MERGE INTO OBJREFERENCE R ")
                .append(" USING (SELECT ? ATTR_ID, ? OBJECT_ID, ? REFERENCE FROM dual) NEW ")
                .append("  ON (R.OBJECT_ID = NEW.OBJECT_ID AND R.ATTR_ID = NEW.ATTR_ID) ")
                .append(" WHEN MATCHED THEN UPDATE ")
                .append("  SET R.REFERENCE = NEW.REFERENCE ")
                .append(" WHEN NOT MATCHED THEN INSERT ")
                .append("  VALUES (NEW.ATTR_ID, NEW.REFERENCE, NEW.OBJECT_ID)");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
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
