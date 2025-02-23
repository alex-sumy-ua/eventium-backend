package com.gamboom.eventium.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Map<String, String> errorResponse = new HashMap<>();

        String errorMessage = exception.getMessage();

        if (errorMessage.contains("user_email_key")) {
            errorResponse.put("message", "The user with this email already exists in the system");
        } else if (errorMessage.contains("unique_registration")) {
            errorResponse.put("message", "The user is already registered for this event");
        } else {
            errorResponse.put("message", "A data integrity occurred" );
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
