package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//import org.slf4j.LoggerFactory;
// import org. slf4j. Logger;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(Event event) {
//        Logger logger = LoggerFactory.getLogger(EventService.class);
        // Log the event's createdBy field before assignment
//        logger.info("Event createdBy before assignment: {}", event.getCreatedBy());
        // Fetch the created_by user ID from the event object
        UUID createdByUserId = event.getCreatedBy();
        // Validate that the user exists in the database
        if (createdByUserId != null) {
            Optional<User> existingUser = userRepository.findById(createdByUserId);
            if (existingUser.isEmpty()) {
//                logger.error("User not found for ID: {}", createdByUserId);
                throw new RuntimeException("User not found in the database.");
            }
        } else {
//            logger.warn("CreatedBy user ID is null.");
        }
        // Set the createdAt timestamp
        event.setCreatedAt(LocalDateTime.now());
        // Save the event to the database
        Event savedEvent = eventRepository.save(event);
//        logger.info("Event saved with ID: {}", savedEvent.getEventId());
//        logger.info("Event createdBy user ID: {}", savedEvent.getCreatedBy());
        return savedEvent;
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
