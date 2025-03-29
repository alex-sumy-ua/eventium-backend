package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.EventRegistrationRepository;
import com.gamboom.eventium.repository.EventRepository;
import com.gamboom.eventium.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    class EventRegistrationServiceTest {

        @Mock
        private EventRegistrationRepository eventRegistrationRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private EventRepository eventRepository;

        @InjectMocks
        private EventRegistrationService eventRegistrationService;

        private User user;
        private Event event;
        private EventRegistration registration;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            user = new User();
            user.setUserId(UUID.randomUUID());
            user.setName("John Doe");

            event = new Event();
            event.setEventId(UUID.randomUUID());
            event.setTitle("Tech Conference");

            registration = new EventRegistration();
            registration.setEventRegistrationId(UUID.randomUUID());
            registration.setUser(user);
            registration.setEvent(event);
            registration.setRegistrationTime(LocalDateTime.now());
        }

        @Test
        @DisplayName("Create registration successfully")
        void createRegistration_ShouldSucceed() {
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
            when(eventRepository.findById(event.getEventId())).thenReturn(Optional.of(event));
            when(eventRegistrationRepository.save(any(EventRegistration.class))).thenReturn(registration);

            Map<String, Object> result = eventRegistrationService.createRegistration(user.getUserId(), event.getEventId(), LocalDateTime.now());

            assertNotNull(result);
            assertEquals(user.getUserId().toString(), result.get("userId"));
            verify(eventRegistrationRepository, times(1)).save(any(EventRegistration.class));
        }

        @Test
        @DisplayName("Get registration by ID")
        void getRegistrationById_ShouldReturnData() {
            when(eventRegistrationRepository.findById(registration.getEventRegistrationId())).thenReturn(Optional.of(registration));

            Map<String, Object> result = eventRegistrationService.getRegistrationById(registration.getEventRegistrationId());

            assertNotNull(result);
            assertEquals(user.getUserId().toString(), result.get("userId"));
        }

        @Test
        @DisplayName("Delete registration")
        void deleteRegistration_ShouldRemoveIt() {
            when(eventRegistrationRepository.findById(registration.getEventRegistrationId())).thenReturn(Optional.of(registration));
            doNothing().when(eventRegistrationRepository).delete(registration);

            eventRegistrationService.deleteRegistration(registration.getEventRegistrationId());

            verify(eventRegistrationRepository, times(1)).delete(registration);
        }

        @Test
        @DisplayName("Get registration by ID - Not Found")
        void getRegistrationById_ShouldThrowException() {
            when(eventRegistrationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> eventRegistrationService.getRegistrationById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Get all registrations - should return formatted list")
        void getAllRegistrations_ShouldReturnFormattedList() {
            // Mock data
            UUID userId = UUID.randomUUID();
            UUID eventId = UUID.randomUUID();
            UUID registrationId = UUID.randomUUID();

            User user = new User();
            user.setUserId(userId);
            user.setName("John Doe");

            Event event = new Event();
            event.setEventId(eventId);
            event.setTitle("Tech Conference");

            EventRegistration registration = new EventRegistration();
            registration.setEventRegistrationId(registrationId);
            registration.setUser(user);
            registration.setEvent(event);
            registration.setRegistrationTime(LocalDateTime.of(2025, 3, 30, 10, 0));

            List<EventRegistration> mockRegistrations = List.of(registration);

            // Mock repository behavior
            when(eventRegistrationRepository.findAll()).thenReturn(mockRegistrations);

            // Call service method
            List<Map<String, Object>> result = eventRegistrationService.getAllRegistrations();

            // Assertions
            assertNotNull(result);
            assertEquals(1, result.size());

            Map<String, Object> firstResult = result.get(0);
            assertEquals(registrationId.toString(), firstResult.get("event_registration_id"));
            assertEquals(userId.toString(), firstResult.get("userId"));
            assertEquals(eventId.toString(), firstResult.get("eventId"));
            assertEquals("2025-03-30T10:00", firstResult.get("registrationDate"));

            // Verify interactions
            verify(eventRegistrationRepository, times(1)).findAll();
        }

    }
