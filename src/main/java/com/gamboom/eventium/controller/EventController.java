package com.gamboom.eventium.controller;


import com.gamboom.eventium.model.Event;
//import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.service.EventService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

//
//    @Autowired
//    private EventRepository eventRepository;

//    @GetMapping
//    public List<Event> getAllEvents() {
//        return eventRepository.findAll();
//    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

//    @GetMapping("/{id}")
//    public Event getEventById(@PathVariable UUID id) {
//        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

//    @PostMapping
//    public Event createEvent(@RequestBody Event event) {
//        return eventRepository.save(event);
//    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event); // ***
//        return ResponseEntity.ok(eventService.createEvent(event));
        return ResponseEntity.status(201).body(createdEvent);
    }

//    @PutMapping("/{id}")
//    public Event updateEvent(@PathVariable UUID id, @RequestBody Event eventDetails) {
//        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
//        event.setTitle(eventDetails.getTitle());
//        event.setDescription(eventDetails.getDescription());
//        event.setLocation(eventDetails.getLocation());
//        event.setStartTime(eventDetails.getStartTime());
//        event.setEndTime(eventDetails.getEndTime());
//        return eventRepository.save(event);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @RequestBody Event eventDetails) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails));
    }

//    @DeleteMapping("/{id}")
//    public void deleteEvent(@PathVariable UUID id) {
//        eventRepository.deleteById(id);
//    }

    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
//        eventService.deleteEvent(id);
//        return ResponseEntity.noContent().build();
//    }
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ResponseEntity.ok("Event deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
    }


}
