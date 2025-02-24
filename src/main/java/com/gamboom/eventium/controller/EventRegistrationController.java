package com.gamboom.eventium.controller;

import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.service.EventRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-registrations")
@Tag(name = "Event Registrations", description = "Handles event registrations")
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    public EventRegistrationController(EventRegistrationService eventRegistrationService) {
        this.eventRegistrationService = eventRegistrationService;
    }

    @Operation(summary = "Register a user for an event", description = "Creates a new event registration")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRegistration(@RequestBody Map<String, Object> request) {
        UUID userId = UUID.fromString((String) request.get("userId"));
        UUID eventId = UUID.fromString((String) request.get("eventId"));
        LocalDateTime registrationTime = LocalDateTime.parse((CharSequence) request.get("registrationDate"));

        Map<String, Object> createdRegistration = eventRegistrationService.createRegistration(userId, eventId, registrationTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
    }

    @Operation(summary = "Get all registrations", description = "Returns a list of all registrations")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllRegistrations() {
        List<Map<String, Object>> registrations = eventRegistrationService.getAllRegistrations();
        return ResponseEntity.ok(registrations);
    }

    @Operation(summary = "Get registration by ID", description = "Fetch a registration by its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRegistrationById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventRegistrationService.getRegistrationById(id));

    }

    @Operation(summary = "Update a registration", description = "Updates a registration data except its unique ID")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRegistration(@PathVariable UUID id,
                                                                  @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(eventRegistrationService.updateRegistration(id, updates));
    }

    @Operation(summary = "Delete a registration", description = "Deletes a registration by its unique ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable UUID id) {
        boolean deleted = eventRegistrationService.deleteRegistration(id);
        if (deleted) {
            return ResponseEntity.ok("Registration deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registration not found");
        }

    }

}