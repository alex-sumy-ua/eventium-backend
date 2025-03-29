package com.gamboom.eventium.repository;

import com.gamboom.eventium.model.Event;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setTitle("Test Event");
        testEvent.setDescription("A test event");
        testEvent.setLocation("Online");
        testEvent.setStartTime(LocalDateTime.now().plusDays(1));
        testEvent.setEndTime(LocalDateTime.now().plusDays(2));

        testEvent = eventRepository.save(testEvent);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    void deleteInBatch() {
        eventRepository.deleteInBatch(eventRepository.findAll());
        assertEquals(0, eventRepository.count());
    }

    @Test
    void testDeleteInBatch() {
        eventRepository.deleteAll();
        assertEquals(0, eventRepository.count());
    }

    @Test
    void saveAndRetrieveEvent() {
        Optional<Event> retrievedEvent = eventRepository.findById(testEvent.getEventId());
        assertTrue(retrievedEvent.isPresent());
        assertEquals("Test Event", retrievedEvent.get().getTitle());
    }

}
