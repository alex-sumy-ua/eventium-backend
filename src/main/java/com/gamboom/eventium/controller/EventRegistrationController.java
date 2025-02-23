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
    public ResponseEntity<EventRegistration> createRegistration(@RequestBody Map<String, Object> requestBody) {
        UUID userId = UUID.fromString((String) requestBody.get("userId"));
        UUID eventId = UUID.fromString((String) requestBody.get("eventId"));
        LocalDateTime registrationTime = requestBody.containsKey("registrationDate")
                ? LocalDateTime.parse((String) requestBody.get("registrationDate"))
                : LocalDateTime.now();
        EventRegistration registration = eventRegistrationService.createRegistration(userId,eventId, registrationTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    @GetMapping
    public ResponseEntity<List<EventRegistration>> getAllRegistrations() {
        return ResponseEntity.ok(eventRegistrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRegistration> getRegistrationById(@PathVariable UUID id) {
        Optional<EventRegistration> registration = eventRegistrationService.getRegistrationById(id);
        return registration
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<EventRegistration> updateRegistration(@PathVariable UUID id,
                                                                @RequestBody EventRegistration updatedRegistration) {
        return ResponseEntity.ok(eventRegistrationService.updateRegistration(id, updatedRegistration));
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