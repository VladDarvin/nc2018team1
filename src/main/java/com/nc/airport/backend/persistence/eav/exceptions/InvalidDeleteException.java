package com.nc.airport.backend.persistence.eav.exceptions;

public class InvalidDeleteException extends RuntimeException {
    public InvalidDeleteException(String message) {
        super(message);
    }
}
