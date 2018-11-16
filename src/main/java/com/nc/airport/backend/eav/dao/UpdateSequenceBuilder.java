package com.nc.airport.backend.eav.dao;


import com.nc.airport.backend.eav.mutable.Mutable;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class UpdateSequenceBuilder extends SequenceBuilder{
    private Mutable mutable;
    private Connection connection;
    private BigInteger objectId;

    UpdateSequenceBuilder(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void build(Mutable mutable){
        this.mutable = mutable;
        objectId = mutable.getObjectId();
        updateObject();
        updateAttributes();
    }

    protected void logSQLError(SQLException e, String inTable){
        logSQLError(e, inTable, "update");
    }

    /**
     * You can not change Object_ID or Object_Type_ID by this method
     * for security purposes.
     */
    protected void updateObject(){
        try (PreparedStatement statement
                 = connection.prepareStatement(
                    "UPDATE OBJECTS SET " +
                         "PARENT_ID = ?, NAME = ?, DESCRIPTION = ?" +
                            "WHERE OBJECT_ID = ?")){

            statement.setObject(1, mutable.getParentId());
            statement.setString(2, mutable.getObjectName());
            statement.setString(3, mutable.getObjectDescription());
            statement.setObject(4, objectId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            logSQLError(e, "Objects");
        }
    }

    protected void updateAttributes(){
        updateValues(mutable.getValues(), "VALUE ");
        updateValues(mutable.getDateValues(), "DATE_VALUE ");
        updateValues(mutable.getListValues(), "LIST_VALUE_ID");
    }

    private void updateValues(Map<BigInteger, ?> values, String valueType){
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
     * Last two method are just there if they will be needed somehow
     * The usage of methods updating ObjReferences is not recommended
     * because of usage only two of three peaces of PK-s,
     * which is leading to possibility of conflicts
     */
    private void updateAtrIdOfObjReferences(){
        Map<BigInteger, BigInteger> references = mutable.getReferences();
        if (noSuchElementsInObject(references)) return;

        String sql = "UPDATE OBJREFERENCE SET ATTR_ID = ?" +
                " WHERE REFERENCE = ? AND OBJECT_ID = " + objectId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<BigInteger, BigInteger> entry : references.entrySet()) {
                statement.setObject(1, entry.getKey());
                statement.setObject(2, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "References");
        }
    }

    private void updateReferencesOfObjReferences(){
        Map<BigInteger, BigInteger> references = mutable.getReferences();
        if (noSuchElementsInObject(references)) return;

        String sql = "UPDATE OBJREFERENCE SET REFERENCE = ?" +
                " WHERE  ATTR_ID = ? AND OBJECT_ID = " + objectId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<BigInteger, BigInteger> entry : references.entrySet()) {
                statement.setObject(1, entry.getValue());
                statement.setObject(2, entry.getKey());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logSQLError(e, "References");
        }
    }


}
