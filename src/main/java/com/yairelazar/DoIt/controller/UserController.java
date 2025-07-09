package com.yairelazar.DoIt.controller;

import com.yairelazar.DoIt.dto.LoginRequest;
import com.yairelazar.DoIt.model.User;
import com.yairelazar.DoIt.service.UserService;
import com.yairelazar.DoIt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.yairelazar.DoIt.dto.LoginResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest registerRequest) {
        try {
            User newUser = userService.register(registerRequest.getUsername(), registerRequest.getPassword());
            String token = jwtUtil.generateToken(newUser.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", newUser.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login request received: username=" + loginRequest.getUsername() + ", password=" + loginRequest.getPassword());

        User loggedInUser = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loggedInUser != null) {
            String token = jwtUtil.generateToken(loggedInUser.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", loggedInUser.getUsername());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        Optional<User> user = userService.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    @GetMapping("/usernames")
    public ResponseEntity<List<String>> getAllUsernames() {
        List<String> usernames = userService.getAllUsernames();
        return ResponseEntity.ok(usernames);
    }
}
