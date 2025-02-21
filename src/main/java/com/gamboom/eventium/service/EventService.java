package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(Event event) {
        event.setCreatedAt(LocalDateTime.now()); // Set createdAt timestamp
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(UUID id) {
        return Optional.of(eventRepository.getReferenceById(id));
    }

    public Event updateEvent(UUID id, Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(eventDetails.getTitle());
                    event.setDescription(eventDetails.getDescription());
                    event.setLocation(eventDetails.getLocation());
                    event.setStartTime(eventDetails.getStartTime());
                    event.setEndTime(eventDetails.getEndTime());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public boolean deleteEvent(UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            eventRepository.delete(event.get());
            return true;
        }
        return false;
    }


}
