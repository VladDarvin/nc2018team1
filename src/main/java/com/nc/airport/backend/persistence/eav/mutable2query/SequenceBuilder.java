package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

abstract class SequenceBuilder {
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
    protected Connection connection;

    public SequenceBuilder(Connection connection) {
        this.connection = connection;
    }

    abstract Mutable build (Mutable mutable) throws SQLException;

    boolean noSuchElementsInObject(Map map) {
        return map == null || map.size() == 0;
    }

    protected void logSQLError(SQLException e, String inTable, String operation) throws SQLException {
        LOGGER.log(Level.ERROR, "Invalid values in mutable for "+operation+" in "+inTable, e);
        throw e;
    }

    protected BigInteger getNewObjectId() throws SQLException {
        try {
            ResultSet nextVal = connection.createStatement()
                    .executeQuery("SELECT COALESCE(MIN(O1.OBJECT_ID+1), 1)\n" +
                            "  FROM OBJECTS O1 LEFT JOIN OBJECTS O2 ON O1.OBJECT_ID + 1 = O2.OBJECT_ID\n" +
                            "  WHERE O2.OBJECT_ID IS NULL");
            nextVal.next();
            return new BigInteger(nextVal.getString(1));
        } catch (SQLException e) {
            LOGGER.error("Failed to get new object id from sequence");
            throw e;
        }
    }
}
