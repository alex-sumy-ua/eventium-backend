package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Role;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUserId(UUID.randomUUID());
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole(Role.MEMBER);
        testUser.setPassword("securepassword");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Create user - should save and return user")
    void createUser_ShouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.createUser(testUser);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Get all users - should return list of users")
    void getAllUsers_ShouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get user by ID - should return user")
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.getUserById(testUser.getUserId());

        assertTrue(user.isPresent());
        assertEquals("John Doe", user.get().getName());
        verify(userRepository, times(1)).findById(testUser.getUserId());
    }

    @Test
    @DisplayName("Get user by ID - should return empty if not found")
    void getUserById_ShouldReturnEmptyIfNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserById(UUID.randomUUID());

        assertTrue(user.isEmpty());
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Update user - should update and return user")
    void updateUser_ShouldUpdateAndReturnUser() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");
        updatedUser.setRole(Role.STAFF);
        updatedUser.setPassword("newpassword");
        updatedUser.setCreatedAt(LocalDateTime.now());

        User result = userService.updateUser(testUser.getUserId(), updatedUser);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals(Role.STAFF, result.getRole());
        verify(userRepository, times(1)).findById(testUser.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user - should throw exception if user not found")
    void updateUser_ShouldThrowExceptionIfNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        User updatedUser = new User();

        assertThrows(RuntimeException.class, () -> userService.updateUser(UUID.randomUUID(), updatedUser));
    }

    @Test
    @DisplayName("Delete user - should delete user and return true")
    void deleteUser_ShouldDeleteUser() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        boolean deleted = userService.deleteUser(testUser.getUserId());

        assertTrue(deleted);
        verify(userRepository, times(1)).findById(testUser.getUserId());
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("Delete user - should return false if user not found")
    void deleteUser_ShouldReturnFalseIfNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUser(UUID.randomUUID());

        assertFalse(deleted);
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Create or update user - should create new user if not exists")
    void createOrUpdateUser_ShouldCreateNewUserIfNotExists() {
        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttribute("email")).thenReturn("newuser@example.com");
        when(mockOAuth2User.getAttribute("name")).thenReturn("New User");
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        // Capture the actual saved user
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            return savedUser; // Return the same user object
        });

        User result = userService.createOrUpdateUser(mockOAuth2User);

        assertNotNull(result);
        assertEquals("New User", result.getName());
        assertEquals("newuser@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("newuser@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Create or update user - should update existing user")
    void createOrUpdateUser_ShouldUpdateExistingUser() {
        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttribute("email")).thenReturn("john.doe@example.com");
        when(mockOAuth2User.getAttribute("name")).thenReturn("Updated John Doe");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createOrUpdateUser(mockOAuth2User);

        assertNotNull(result);
        assertEquals("Updated John Doe", result.getName());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Get user by email - should return user if exists")
    void getUserByEmail_ShouldReturnUserIfExists() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.getUserByEmail("john.doe@example.com");

        assertTrue(user.isPresent());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Get user by email - should return empty if not found")
    void getUserByEmail_ShouldReturnEmptyIfNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserByEmail("notfound@example.com");

        assertTrue(user.isEmpty());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

}
