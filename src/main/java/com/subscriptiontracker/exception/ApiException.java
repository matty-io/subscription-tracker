package com.subscriptiontracker.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class ApiException extends RuntimeException {
    private String message;
    private int status = 400;
    private Map<String, String> errors;
}
