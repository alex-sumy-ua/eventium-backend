package com.gamboom.eventium.repository;

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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole(Role.MEMBER);
        testUser.setCreatedAt(LocalDateTime.now());

        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void deleteInBatch() {
        userRepository.deleteInBatch(userRepository.findAll());
        assertEquals(0, userRepository.count());
    }

    @Test
    void findByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void findByEmail_NotFound() {
        Optional<User> user = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(user.isPresent());
    }

}
