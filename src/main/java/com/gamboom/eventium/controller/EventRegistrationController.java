package com.gamboom.eventium.controller;

import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.service.EventRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-registrations")
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    public EventRegistrationController(EventRegistrationService eventRegistrationService) {
        this.eventRegistrationService = eventRegistrationService;
    }

    @PostMapping
    public ResponseEntity<EventRegistration> createRegistration(@RequestBody EventRegistration registration) {
        return ResponseEntity.ok(eventRegistrationService.createRegistration(registration));
    }

    @GetMapping
    public ResponseEntity<List<EventRegistration>> getAllRegistrations() {
        return ResponseEntity.ok(eventRegistrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRegistration> getRegistrationById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventRegistrationService.getRegistrationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventRegistration> updateRegistration(@PathVariable UUID id, @RequestBody EventRegistration updatedRegistration) {
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