package com.gamboom.eventium.controller;

import com.gamboom.eventium.model.Role;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private User testUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setUserId(userId);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole(Role.MEMBER);
        testUser.setPassword("hashedpassword");

        userList = Collections.singletonList(testUser);
    }

    @Test
    @DisplayName("Create User - Should return created user")
    void createUser_ShouldReturnCreatedUser() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser(testUser);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Get All Users - Should return a list of users")
    void getAllUsers_ShouldReturnUserList() {
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userList, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Get User by ID - Should return user")
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(userId)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Get User by ID - Should return 404 if not found")
    void getUserById_ShouldReturnNotFound() {
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Get User by Email - Should return user")
    void getUserByEmail_ShouldReturnUser() {
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserByEmail("john.doe@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Get User by Email - Should return 404 if not found")
    void getUserByEmail_ShouldReturnNotFound() {
        when(userService.getUserByEmail("unknown@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmail("unknown@example.com");

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).getUserByEmail("unknown@example.com");
    }

    @Test
    @DisplayName("Update User - Should return updated user")
    void updateUser_ShouldReturnUpdatedUser() {
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.updateUser(userId, testUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("Delete User - Should return success message")
    void deleteUser_ShouldReturnSuccess() {
        when(userService.deleteUser(userId)).thenReturn(true);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Delete User - Should return not found")
    void deleteUser_ShouldReturnNotFound() {
        when(userService.deleteUser(userId)).thenReturn(false);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }

}
