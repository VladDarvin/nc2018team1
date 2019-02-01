package com.nc.airport.backend.persistence.eav.exceptions;

public class DatabaseConsistencyException extends DatabaseConnectionException {

    public DatabaseConsistencyException(String message) {
        super(message);
    }
}
