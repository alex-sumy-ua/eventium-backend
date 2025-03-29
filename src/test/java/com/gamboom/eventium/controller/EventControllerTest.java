package com.gamboom.eventium.controller;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEvent = new Event();
        testEvent.setEventId(UUID.randomUUID());
        testEvent.setTitle("Community Meetup");
        testEvent.setDescription("A meetup for local community members.");
        testEvent.setLocation("Community Hall");
        testEvent.setStartTime(LocalDateTime.now().plusDays(3));
        testEvent.setEndTime(LocalDateTime.now().plusDays(3).plusHours(2));
    }

    @Test
    @DisplayName("Get all events - should return list of events")
    void getAllEvents_ShouldReturnList() {
        when(eventService.getAllEvents()).thenReturn(List.of(testEvent));

        ResponseEntity<List<Event>> response = eventController.getAllEvents();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Community Meetup", response.getBody().get(0).getTitle());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    @DisplayName("Get event by ID - should return event")
    void getEventById_ShouldReturnEvent() {
        UUID eventId = testEvent.getEventId();
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(testEvent));

        ResponseEntity<Event> response = eventController.getEventById(eventId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Community Meetup", response.getBody().getTitle());
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Get event by ID - should return 404 if not found")
    void getEventById_ShouldReturnNotFound() {
        UUID eventId = UUID.randomUUID();
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            eventController.getEventById(eventId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Create event - should return created event")
    void createEvent_ShouldReturnCreatedEvent() {
        when(eventService.createEvent(any(Event.class))).thenReturn(testEvent);

        ResponseEntity<Event> response = eventController.createEvent(testEvent);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Community Meetup", response.getBody().getTitle());
        verify(eventService, times(1)).createEvent(any(Event.class));
    }

    @Test
    @DisplayName("Update event - should return updated event")
    void updateEvent_ShouldReturnUpdatedEvent() {
        UUID eventId = testEvent.getEventId();
        when(eventService.updateEvent(eq(eventId), any(Event.class))).thenReturn(testEvent);

        ResponseEntity<Event> response = eventController.updateEvent(eventId, testEvent);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Community Meetup", response.getBody().getTitle());
        verify(eventService, times(1)).updateEvent(eq(eventId), any(Event.class));
    }

    @Test
    @DisplayName("Delete event - should return success message")
    void deleteEvent_ShouldReturnSuccessMessage() {
        UUID eventId = testEvent.getEventId();
        when(eventService.deleteEvent(eventId)).thenReturn(true);

        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event deleted successfully", response.getBody());
        verify(eventService, times(1)).deleteEvent(eventId);
    }

    @Test
    @DisplayName("Delete event - should return 404 if event not found")
    void deleteEvent_ShouldReturnNotFound() {
        UUID eventId = UUID.randomUUID();
        when(eventService.deleteEvent(eventId)).thenReturn(false);

        ResponseEntity<String> response = eventController.deleteEvent(eventId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found", response.getBody());
        verify(eventService, times(1)).deleteEvent(eventId);
    }

}
