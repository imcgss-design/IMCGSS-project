package com.imcgss.imcgss_backend;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerStudent(User user, String password) {

        if (user.getUserId() == null || user.getUserId().isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long.");
        }

        String userId = user.getUserId().trim();
        String email = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "An account already exists for this user ID.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account already exists for this email.");
        }

        user.setUserId(userId);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Students cannot choose their own role or account status.
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");
        user.setDepartment(null);

        return userRepository.save(user);
    }

    public User login(String userId, String password) {

        if (userId == null || userId.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "User ID and password are required.");
        }

        User user = userRepository.findByUserId(userId.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid user ID or password."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException(
                    "Invalid user ID or password.");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalArgumentException(
                    "This account is not active. Contact the administrator.");
        }

        return user;
    }
}
