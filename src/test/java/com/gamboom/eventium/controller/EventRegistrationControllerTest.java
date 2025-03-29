package com.gamboom.eventium.controller;

import com.gamboom.eventium.service.EventRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventRegistrationControllerTest {

    @Mock
    private EventRegistrationService eventRegistrationService;

    @InjectMocks
    private EventRegistrationController eventRegistrationController;

    private UUID userId;
    private UUID eventId;
    private UUID registrationId;
    private Map<String, Object> registrationData;
    private List<Map<String, Object>> registrationList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        registrationId = UUID.randomUUID();

        registrationData = new HashMap<>();
        registrationData.put("event_registration_id", registrationId.toString());
        registrationData.put("userId", userId.toString());
        registrationData.put("eventId", eventId.toString());
        registrationData.put("registrationDate", LocalDateTime.now().toString());

        registrationList = Collections.singletonList(registrationData);
    }

    @Test
    @DisplayName("Create Registration - Should return created registration")
    void createRegistration_ShouldReturnCreatedRegistration() {
        when(eventRegistrationService.createRegistration(eq(userId), eq(eventId), any(LocalDateTime.class)))
                .thenReturn(registrationData);

        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId.toString());
        request.put("eventId", eventId.toString());
        request.put("registrationDate", LocalDateTime.now().toString());

        ResponseEntity<Map<String, Object>> response = eventRegistrationController.createRegistration(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(registrationData, response.getBody());
        verify(eventRegistrationService, times(1)).createRegistration(eq(userId), eq(eventId), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Get All Registrations - Should return a list of registrations")
    void getAllRegistrations_ShouldReturnList() {
        when(eventRegistrationService.getAllRegistrations()).thenReturn(registrationList);

        ResponseEntity<List<Map<String, Object>>> response = eventRegistrationController.getAllRegistrations();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(registrationList, response.getBody());
        verify(eventRegistrationService, times(1)).getAllRegistrations();
    }

    @Test
    @DisplayName("Get Registration by ID - Should return registration")
    void getRegistrationById_ShouldReturnRegistration() {
        when(eventRegistrationService.getRegistrationById(registrationId)).thenReturn(registrationData);

        ResponseEntity<Map<String, Object>> response = eventRegistrationController.getRegistrationById(registrationId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(registrationData, response.getBody());
        verify(eventRegistrationService, times(1)).getRegistrationById(registrationId);
    }

    @Test
    @DisplayName("Update Registration - Should return updated registration")
    void updateRegistration_ShouldReturnUpdatedRegistration() {
        Map<String, Object> updatedData = new HashMap<>(registrationData);
        updatedData.put("registrationDate", LocalDateTime.now().plusDays(1).toString());

        when(eventRegistrationService.updateRegistration(eq(registrationId), anyMap())).thenReturn(updatedData);

        ResponseEntity<Map<String, Object>> response = eventRegistrationController.updateRegistration(registrationId, updatedData);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedData, response.getBody());
        verify(eventRegistrationService, times(1)).updateRegistration(eq(registrationId), anyMap());
    }

    @Test
    @DisplayName("Delete Registration - Should return success message")
    void deleteRegistration_ShouldReturnSuccess() {
        when(eventRegistrationService.deleteRegistration(registrationId)).thenReturn(true);

        ResponseEntity<String> response = eventRegistrationController.deleteRegistration(registrationId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registration deleted successfully", response.getBody());
        verify(eventRegistrationService, times(1)).deleteRegistration(registrationId);
    }

    @Test
    @DisplayName("Delete Registration - Should return not found")
    void deleteRegistration_ShouldReturnNotFound() {
        when(eventRegistrationService.deleteRegistration(registrationId)).thenReturn(false);

        ResponseEntity<String> response = eventRegistrationController.deleteRegistration(registrationId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Registration not found", response.getBody());
        verify(eventRegistrationService, times(1)).deleteRegistration(registrationId);
    }

}
