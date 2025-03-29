package com.gamboom.eventium.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        mockRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Handle duplicate email - should return conflict response")
    void handleDuplicateEmail_ShouldReturnConflict() {
        when(mockRequest.getDescription(false)).thenReturn("/api/users");

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("ERROR: duplicate key value violates unique constraint \"user_email_key\"");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleDataIntegrityViolationException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The user with this email already exists in the system", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Handle duplicate event registration - should return conflict response")
    void handleDuplicateEventRegistration_ShouldReturnConflict() {
        when(mockRequest.getDescription(false)).thenReturn("/api/event-registrations");

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("ERROR: duplicate key value violates unique constraint \"unique_registration\"");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleDataIntegrityViolationException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The user is already registered for this event", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Handle generic data integrity violation - should return conflict response")
    void handleGenericDataIntegrityViolation_ShouldReturnConflict() {
        when(mockRequest.getDescription(false)).thenReturn("/api/events");

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("ERROR: some unknown data integrity issue");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleDataIntegrityViolationException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("A data integrity violation occurred", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Ignore Swagger and OpenAPI requests")
    void handleSwaggerRequest_ShouldReturnNull() {
        when(mockRequest.getDescription(false)).thenReturn("/v3/api-docs");

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("ERROR: Swagger test");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleDataIntegrityViolationException(exception, mockRequest);

        assertNull(response, "Expected Swagger requests to be ignored");
    }

}
