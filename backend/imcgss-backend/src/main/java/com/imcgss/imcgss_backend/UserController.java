package com.imcgss.imcgss_backend;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        // Clear passwords before returning to client
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        if (user.getUserId() == null || user.getUserId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User ID already exists");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            // Assign default password if none provided
            user.setPassword(passwordEncoder.encode("Default123!"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getStatus() == null || user.getStatus().isBlank()) {
            user.setStatus("ACTIVE");
        }

        User saved = userRepository.save(user);
        saved.setPassword(null);
        return saved;
    }
}
