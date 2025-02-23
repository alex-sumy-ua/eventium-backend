package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.EventRegistrationRepository;
import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // Common response format
    private Map<String, Object> formatResponse(EventRegistration registration) {
        Map<String, Object> response = new HashMap<>();
        response.put("userId", registration.getUser().getUserId().toString());
        response.put("eventId", registration.getEvent().getEventId().toString());
        response.put("registrationDate", registration.getRegistrationTime().toString());
        return  response;
    }

    public Map<String, Object> createRegistration(UUID userId, UUID eventId, LocalDateTime registrationTime) {
        // Fetch the User by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch the Event by ID
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventRegistration registration = new EventRegistration();

        // Set fetched user and event
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationTime(registrationTime);

        EventRegistration savedRegistration = eventRegistrationRepository.save(registration);

        return formatResponse(savedRegistration);

    }

    public List<Map<String, Object>> getAllRegistrations() {
        return eventRegistrationRepository.findAll()
                .stream()
                .map(this::formatResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getRegistrationById(UUID id) {
        return eventRegistrationRepository.findById(id)
                .map(this::formatResponse)
                .orElseThrow(() -> new RuntimeException("Event registration not found"));
    }

    public Map<String, Object> updateRegistration(UUID id, Map<String, Object> updates) {
        return eventRegistrationRepository.findById(id)
                .map(existingRegistration -> {
                    // Fetch User and Event based on IDs from the request
                    UUID userId = UUID.fromString((String) updates.get("userId"));
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    UUID eventId = UUID.fromString((String) updates.get("eventId"));
                    Event event = eventRepository.findById(eventId)
                            .orElseThrow(() -> new RuntimeException("Event not found"));

                    String dateStr = (String) updates.get("registrationDate");
                    LocalDateTime registrationTime = LocalDateTime.parse(dateStr);

                    // Update fields
                    existingRegistration.setUser(user);
                    existingRegistration.setEvent(event);
                    existingRegistration.setRegistrationTime(registrationTime);

                    EventRegistration updatedRegistration = eventRegistrationRepository.save(existingRegistration);
//                    eventRegistrationRepository.save(existingRegistration);
//
//                    // Construct response matching request format
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("userId", userId.toString());
//                    response.put("eventId", eventId.toString());
//                    response.put("registrationDate", dateStr);
//
//                    return response;
                    return formatResponse(updatedRegistration);
                })
                .orElseThrow(() -> new RuntimeException("Event registration not found"));
    }

    public boolean deleteRegistration(UUID id) {
//        Optional<EventRegistration> registration = eventRegistrationRepository.findById(id);
//        if (registration.isPresent()) {
//            eventRegistrationRepository.delete(registration.get());
//            return true;
//        }
//        return false;
        return eventRegistrationRepository.findById(id)
                .map(registration -> {
                    eventRegistrationRepository.delete(registration);
                    return true;
                })
                .orElse(false);
    }


}
