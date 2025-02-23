package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.EventRegistrationRepository;
import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public EventRegistration createRegistration(UUID userId, UUID eventId, LocalDateTime registrationTime) {
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

        return eventRegistrationRepository.save(registration);
    }

    public List<EventRegistration> getAllRegistrations() {
        return eventRegistrationRepository.findAll();
    }

    public Optional<EventRegistration> getRegistrationById(UUID id) {
        return eventRegistrationRepository.findById(id);
    }
    public EventRegistration updateRegistration(UUID id, EventRegistration updatedRegistration) {
        Optional<EventRegistration> existingRegistration = eventRegistrationRepository.findById(id);
        if (existingRegistration.isPresent()) {
            EventRegistration registration = existingRegistration.get();
            registration.setUser(updatedRegistration.getUser());
            registration.setEvent(updatedRegistration.getEvent());
            registration.setRegistrationTime(updatedRegistration.getRegistrationTime());
            return eventRegistrationRepository.save(registration);
        } else {
            throw new RuntimeException("Event registration not found");
        }
    }

    public boolean deleteRegistration(UUID id) {
        Optional<EventRegistration> registration = eventRegistrationRepository.findById(id);
        if (registration.isPresent()) {
            eventRegistrationRepository.delete(registration.get());
            return true;
        }
        return false;
    }


}
