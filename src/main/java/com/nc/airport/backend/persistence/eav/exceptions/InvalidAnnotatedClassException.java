package com.nc.airport.backend.persistence.eav.exceptions;

public class InvalidAnnotatedClassException extends RuntimeException {
    private Class causeClass;

    public InvalidAnnotatedClassException(String message, Class causeClass) {
        super(message);
        this.causeClass = causeClass;
    }

    public InvalidAnnotatedClassException(String message, Class causeClass, Throwable ex) {
        super(message, ex);
        this.causeClass = causeClass;
    }

    public Class getCauseClass() {
        return causeClass;
    }
}
