package com.nc.airport.backend.util.exceptions;

public class PrintException extends RuntimeException {

    public PrintException() {
        super();
    }

    public PrintException(String message) {
        super(message);
    }

    public PrintException(Throwable cause) {
        super(cause);
    }

    public PrintException(String message, Throwable cause) {
        super(message, cause);
    }
}
