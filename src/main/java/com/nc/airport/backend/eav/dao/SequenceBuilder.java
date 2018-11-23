package com.nc.airport.backend.eav.dao;

import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

abstract class SequenceBuilder {
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());


    abstract void build (Mutable mutable);

    boolean noSuchElementsInObject(Map map){
        return map.size() == 0;
    }

    protected void logSQLError(SQLException e, String inTable, String operation){
        LOGGER.log(Level.ERROR, "Invalid values in mutable for "+operation+" in "+inTable, e);
    }
}
