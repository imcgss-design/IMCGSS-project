package com.imcgss.imcgss_backend;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request) {

        try {
            String password = request.get("password");

            User user = new User();
            user.setUserId(request.get("userId"));
            user.setFullName(request.get("fullName"));
            user.setEmail(request.get("email"));

            User savedUser = authService.registerStudent(user, password);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Student account created successfully.",
                    "userId", savedUser.getUserId(),
                    "fullName", savedUser.getFullName(),
                    "role", savedUser.getRole()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        try {
            User user = authService.login(
                    request.get("userId"),
                    request.get("password")
            );

            // Never return the password or password hash.
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful.",
                    "userId", user.getUserId(),
                    "fullName", user.getFullName(),
                    "role", user.getRole()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}
