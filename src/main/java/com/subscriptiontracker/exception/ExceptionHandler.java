package com.subscriptiontracker.exception;

import java.util.*;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Whenever unhandled exception happens in controller then this will be triggered, corresponding method executes.
 */
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        List<String> generalErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldErr) {
                String fieldName = fieldErr.getField();
                String errorMessage = fieldErr.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                generalErrors.add(error.getDefaultMessage());
            }
        });

        HttpErrorResponse response = HttpErrorResponse.of("Unprocessable entity", 422, errors, generalErrors);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpErrorResponse> handleException(ApiException e) {
        log.info("Handling ApiException: {}", e.getMessage());
        var response = HttpErrorResponse.of(e.getMessage(), e.getStatus(), e.getErrors(), null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatus()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpErrorResponse> handleException(BadCredentialsException e) {
        log.info("Handling BadCredentialsException: {}", e.getMessage());
        var response = HttpErrorResponse.of(e.getMessage(), 401, null, null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(401));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<HttpErrorResponse> handleException(AuthorizationDeniedException e) {
        log.info("Handling AuthorizationDeniedException: {}", e.getMessage());
        var response = HttpErrorResponse.of(e.getMessage(), 403, null, null);
        return new ResponseEntity<>(response, HttpStatus.valueOf(403));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception", e);
        var response = HttpErrorResponse.of("Unexpected error : " + e.getMessage(), 500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpErrorResponse> handleException(ResourceNotFoundException e) {
        log.info("Handling ResourceNotFoundException: {}", e.getMessage());
        var response = HttpErrorResponse.of(e.getMessage(), 404, null, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpErrorResponse> handleException(AccessDeniedException e) {
        log.info("Handling AccessDeniedException: {}", e.getMessage());
        var response = HttpErrorResponse.of("Access Denied: " + e.getMessage(), 403, null, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Add this handler method for DisabledException
    @org.springframework.web.bind.annotation.ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpErrorResponse> handleException(DisabledException e) {
        log.info("Handling DisabledException: {}", e.getMessage());
        var response = HttpErrorResponse.of("Account is disabled or not activated. Please contact support.", 403, null, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpErrorResponse> handleException(IllegalArgumentException e) {
        log.info("Handling IllegalArgumentException: {}", e.getMessage());
        var response = HttpErrorResponse.of("Invalid input: " + e.getMessage(), 400, null, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.warn("Handling HttpMessageNotReadableException: {}", ex.getMessage());

        String errorMessage = "Invalid request format";
        List<String> generalErrors = new ArrayList<>();

        // Check if the exception is caused by an invalid enum value
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType != null && targetType.isEnum()) {
                Object[] validValues = targetType.getEnumConstants();
                errorMessage = "Invalid value for " + targetType.getSimpleName() +
                        ". Allowed values: " + Arrays.toString(validValues);
            }
        }

        generalErrors.add(errorMessage);
        var response = HttpErrorResponse.of("Bad Request", 400, null, generalErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<HttpErrorResponse> handleException(InternalAuthenticationServiceException e) {
        log.error("Authentication service error: {}", e.getMessage());

        var response = HttpErrorResponse.of("Authentication service error. Please try again later. " + e.getMessage(), 500, null, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
