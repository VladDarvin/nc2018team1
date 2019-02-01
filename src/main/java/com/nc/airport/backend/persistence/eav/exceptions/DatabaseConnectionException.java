package com.nc.airport.backend.persistence.eav.exceptions;

import java.sql.SQLException;

public class DatabaseConnectionException extends RuntimeException {

    protected DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(SQLException sqlException) {
        super(sqlException);
    }

    public DatabaseConnectionException(String message, SQLException sqlException) {
        super(message, sqlException);
    }

    public SQLException getSQLException() {
        return (SQLException) super.getCause();
    }
}
