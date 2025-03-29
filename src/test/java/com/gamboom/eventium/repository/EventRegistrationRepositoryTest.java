package com.gamboom.eventium.repository;

import com.gamboom.eventium.model.Event;
import com.gamboom.eventium.model.EventRegistration;
import com.gamboom.eventium.model.Role;
import com.gamboom.eventium.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EventRegistrationRepositoryTest {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Event testEvent;
    private EventRegistration testRegistration;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Alice");
        testUser.setEmail("alice@example.com");
        testUser.setRole(Role.MEMBER);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser = userRepository.save(testUser);

        testEvent = new Event();
        testEvent.setTitle("Tech Conference");
        testEvent.setDescription("Annual tech conference");
        testEvent.setLocation("New York");
        testEvent.setStartTime(LocalDateTime.now().plusDays(5));
        testEvent.setEndTime(LocalDateTime.now().plusDays(6));
        testEvent.setCreatedBy(testUser.getUserId());
        testEvent = eventRepository.save(testEvent);

        testRegistration = new EventRegistration();
        testRegistration.setUser(testUser);
        testRegistration.setEvent(testEvent);
        testRegistration.setRegistrationTime(LocalDateTime.now());
        testRegistration = eventRegistrationRepository.save(testRegistration);
    }

    @AfterEach
    void tearDown() {
        eventRegistrationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void deleteInBatch() {
        eventRegistrationRepository.deleteInBatch(eventRegistrationRepository.findAll());
        assertEquals(0, eventRegistrationRepository.count());
    }

    @Test
    void saveAndRetrieveRegistration() {
        Optional<EventRegistration> retrievedRegistration = eventRegistrationRepository.findById(testRegistration.getEventRegistrationId());
        assertTrue(retrievedRegistration.isPresent());
        assertEquals("Tech Conference", retrievedRegistration.get().getEvent().getTitle());
        assertEquals("Alice", retrievedRegistration.get().getUser().getName());
    }

}
