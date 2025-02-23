package com.gamboom.eventium.controller;

import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.service.EventRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-registrations")
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    public EventRegistrationController(EventRegistrationService eventRegistrationService) {
        this.eventRegistrationService = eventRegistrationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRegistration(@RequestBody Map<String, Object> request) {
        UUID userId = UUID.fromString((String) request.get("userId"));
        UUID eventId = UUID.fromString((String) request.get("eventId"));
        LocalDateTime registrationTime = LocalDateTime.parse((CharSequence) request.get("registrationDate"));

        Map<String, Object> createdRegistration = eventRegistrationService.createRegistration(userId, eventId, registrationTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllRegistrations() {
        List<Map<String, Object>> registrations = eventRegistrationService.getAllRegistrations();
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRegistrationById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventRegistrationService.getRegistrationById(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRegistration(@PathVariable UUID id,
                                                                  @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(eventRegistrationService.updateRegistration(id, updates));
    }

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