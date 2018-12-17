package com.nc.airport.backend.persistence.eav.exceptions;

import java.sql.SQLException;

/**
 * Created by User on 17.12.2018.
 */
public class BadDBRequestException extends RuntimeException {

    public BadDBRequestException(SQLException sqlException) {
        super(sqlException);
    }

    public BadDBRequestException(String message, SQLException sqlException) {
        super(message, sqlException);
    }

    public SQLException getSQLException() {
        return (SQLException) super.getCause();
    }
}
