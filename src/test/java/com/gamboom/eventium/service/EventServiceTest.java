package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testEvent = new Event();
        testEvent.setEventId(UUID.randomUUID());
        testEvent.setTitle("Community Meetup");
        testEvent.setDescription("A meetup for developers.");
        testEvent.setLocation("Tech Hub");
        testEvent.setStartTime(LocalDateTime.now().plusDays(1));
        testEvent.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
    }

    @Test
    @DisplayName("Get all events - should return a list")
    void getAllEvents_ShouldReturnEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(testEvent));

        List<Event> events = eventService.getAllEvents();

        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get event by ID - should return an event")
    void getEventById_ShouldReturnEvent() {
        UUID eventId = testEvent.getEventId();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(testEvent));

        Optional<Event> event = eventService.getEventById(eventId);

        assertTrue(event.isPresent());
        assertEquals("Community Meetup", event.get().getTitle());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    @DisplayName("Create event - should save and return the event")
    void createEvent_ShouldSaveEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event savedEvent = eventService.createEvent(testEvent);

        assertNotNull(savedEvent);
        assertEquals("Community Meetup", savedEvent.getTitle());
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    @DisplayName("Update event - should update and return the event")
    void updateEvent_ShouldUpdateEvent() {
        UUID eventId = testEvent.getEventId();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event updatedEvent = eventService.updateEvent(eventId, testEvent);

        assertNotNull(updatedEvent);
        assertEquals("Community Meetup", updatedEvent.getTitle());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    @DisplayName("Delete event - should remove event")
    void deleteEvent_ShouldDeleteEvent() {
        UUID eventId = testEvent.getEventId();

        // Mocking findById to return the test event
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(testEvent));
        doNothing().when(eventRepository).delete(testEvent);  // Mock delete method

        // Act
        boolean deleted = eventService.deleteEvent(eventId);

        // Verify correct interactions
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).delete(testEvent);

        // Assert result
        assertTrue(deleted);
    }

    @Test
    @DisplayName("Get event by ID - should return empty if event does not exist")
    void getEventById_ShouldReturnEmptyIfNotFound() {
        UUID nonExistentEventId = UUID.randomUUID();
        when(eventRepository.findById(nonExistentEventId)).thenReturn(Optional.empty());

        Optional<Event> event = eventService.getEventById(nonExistentEventId);

        assertFalse(event.isPresent());
        verify(eventRepository, times(1)).findById(nonExistentEventId);
    }

}
