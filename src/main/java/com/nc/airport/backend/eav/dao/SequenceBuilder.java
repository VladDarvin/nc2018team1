package com.nc.airport.backend.eav.dao;

import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;

abstract class SequenceBuilder {
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());


    abstract Mutable build (Mutable mutable) throws SQLException;

    boolean noSuchElementsInObject(Map map) {
        return map == null || map.size() == 0;
    }

    protected void logSQLError(SQLException e, String inTable, String operation) throws SQLException {
        LOGGER.log(Level.ERROR, "Invalid values in mutable for "+operation+" in "+inTable, e);
        throw e;
    }
}
