package com.gamboom.eventium.service;

import com.gamboom.eventium.model.Role;
import com.gamboom.eventium.model.User;
import com.gamboom.eventium.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(UUID id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setCreatedAt(updatedUser.getCreatedAt());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    public User createOrUpdateUser(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {

            // Update existing user
            User user = existingUser.get();
            user.setName(oauth2User.getAttribute("name"));
            return userRepository.save(user);
        } else {

            // Create new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(oauth2User.getAttribute("name"));
            newUser.setRole(isStaff(email) ? Role.STAFF : Role.MEMBER); // Assign role
            newUser.setCreatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        }
    }

    private boolean isStaff(String email) {

        // Define a list of staff emails (or fetch from a database)
        return List.of("staff1@example.com", "staff2@example.com").contains(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
