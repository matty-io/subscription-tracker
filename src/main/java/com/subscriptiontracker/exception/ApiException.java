package com.subscriptiontracker.exception;

import lombok.Builder;

import java.util.Map;

@Builder
public class ApiException extends RuntimeException {
    private String message;
    private int status = 400;
    private Map<String, String> errors;
}
