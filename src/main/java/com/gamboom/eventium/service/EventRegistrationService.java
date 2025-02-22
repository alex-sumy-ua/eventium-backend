package com.gamboom.eventium.service;

import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.repository.EventRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;

    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    public EventRegistration createRegistration(EventRegistration registration) {
        return eventRegistrationRepository.save(registration);
    }

    public List<EventRegistration> getAllRegistrations() {
        return eventRegistrationRepository.findAll();
    }

    public EventRegistration getRegistrationById(UUID id) {
        return eventRegistrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event registration not found"));
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
