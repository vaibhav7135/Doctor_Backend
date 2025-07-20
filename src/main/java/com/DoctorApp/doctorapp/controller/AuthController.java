package com.DoctorApp.doctorapp.controller;

import com.DoctorApp.doctorapp.dto.LoginRequest;
import com.DoctorApp.doctorapp.dto.RegisterRequest;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.UserRepository;
import com.DoctorApp.doctorapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    // ✅ Manual constructor instead of @RequiredArgsConstructor
    public AuthController(AuthService authService,UserRepository userRepository) {
        this.authService = authService;
        this.userRepository= userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@ModelAttribute RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Registration successful");
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().body("File upload error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\", \"message\": \"Login successful\"}");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    
   


}
