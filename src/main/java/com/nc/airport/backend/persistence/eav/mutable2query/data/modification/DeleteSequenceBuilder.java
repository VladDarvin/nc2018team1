package com.nc.airport.backend.persistence.eav.mutable2query.data.modification;

import com.nc.airport.backend.persistence.eav.Mutable;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteSequenceBuilder extends SequenceBuilder {
    public DeleteSequenceBuilder(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public Mutable build(Mutable mutable) {
        deleteObject(mutable.getObjectId());
        return mutable;
    }

    public Mutable build(BigInteger objectId) {
        deleteObject(objectId);
        Mutable mutable = new Mutable();
        mutable.setObjectId(objectId);
        return mutable;
    }

    private void logSQLError(SQLException e, String inTable) {
        logSQLError(e, inTable, "Deletion");
    }

    private void deleteObject(BigInteger objectId) {
        try (PreparedStatement query = connection.prepareStatement(
                "DELETE FROM OBJECTS WHERE OBJECT_ID = ?")) {

            query.setObject(1, objectId);
            query.executeUpdate();
        } catch (SQLException e) {
            logSQLError(e, "Objects");
        }

        /* Old stuff that have been used to delete attributes and references before the object
         *  Is convenient when foreign key constraint is set to ON RESTRICT*/

//    private void deleteAttributes() {
//        deleteValues(mutable.getValues());
//        deleteValues(mutable.getDateValues());
//        deleteValues(mutable.getListValues());
//    }
//
//    private void deleteValues(Map<BigInteger, ?> values) {
//        if (noSuchElementsInObject(values)) return;
//
//        try (PreparedStatement query = connection.prepareStatement(
//                "DELETE FROM ATTRIBUTES WHERE OBJECT_ID = ?")) {
//            query.setObject(1, objectId);
//            query.executeUpdate();
//        }
//        catch (SQLException e){
//            logSQLError(e, "Attributes");
//        }
//    }
//
//    private void deleteObjReferences() {
//        Map<BigInteger, BigInteger> references = mutable.getReferences();
//
//        if (noSuchElementsInObject(references)) return;
//
//        try (PreparedStatement query = connection.prepareStatement(
//                "DELETE FROM OBJREFERENCE WHERE OBJECT_ID = ?")) {
//            query.setObject(1, objectId);
//            query.executeUpdate();
//        } catch (SQLException e) {
//            logSQLError(e, "References");
//        }
//    }
    }
}
