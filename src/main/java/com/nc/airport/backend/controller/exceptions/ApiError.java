package com.nc.airport.backend.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiError {
    /**
     * @param status holds the operation call status.
     * It will be anything from 4xx to signalize client errors or 5xx to mean server errors.
     * A common scenario is a http code 400 that means a BAD_REQUEST,
     * when the client, for example, sends an improperly formatted field, like an invalid email address.
     * @param timestamp holds the date-time instance of when the error happened.
     * @param message holds a user-friendly message about the error.
     * @param debugMessage holds a system message describing the error in more detail.
     * @param subErrors holds an array of sub-errors that happened.
     * This is used for representing multiple errors in a single call.
     * An example would be validation errors in which multiple fields have failed the validation.
     */
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status, "Unexpected or unknown error", ex);
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
