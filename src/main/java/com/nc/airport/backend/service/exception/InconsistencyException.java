package com.nc.airport.backend.service.exception;

public class InconsistencyException extends RuntimeException {
    public InconsistencyException(String message) {
        super(message);
    }
}
