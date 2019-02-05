package com.nc.airport.backend.controller.exceptions;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.exceptions.InvalidAnnotatedClassException;
import com.nc.airport.backend.security.controller.AuthenticationException;
import com.nc.airport.backend.service.exception.InconsistencyException;
import com.nc.airport.backend.service.exception.ItemNotFoundException;
import com.nc.airport.backend.service.exception.PersistenceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle all exceptions that don’t have specific exception handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(Exception ex) {
        log.error(ex);

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    /*
     * App related exceptions handlers
     */

    @ExceptionHandler({
            PersistenceException.class,
            InconsistencyException.class
    })
    public ResponseEntity<Object> handlePersistenceException(RuntimeException ex) {
        log.error(ex);

        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }



    @ExceptionHandler({
            DatabaseConnectionException.class,
            BadDBRequestException.class,
            InvalidAnnotatedClassException.class,
            ItemNotFoundException.class
    })
    public ResponseEntity<Object> handleDatabaseConnectionException(RuntimeException ex) {
        return defaultExceptionHandler(ex);
    }

    /**
     * Handle exception when user is disabled or has bad credentials.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        log.error(ex);

        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle exception when method arguments are not the expected type
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error("Request : {}", request, ex);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
//        apiError.setMessage(String.format(
//                "The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle exception when API is not able to read the HTTP message
     */
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("Headers : {}; Status : {}; Request : {}",
                headers, status, request, ex);

        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handle exception when JSON is invalid as well.
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.error("Headers : {}; Status : {}; Request : {}",
                headers, status, request, ex);

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }


    /*
     * Serving methods
     */

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
