package com.DoctorApp.doctorapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.DoctorApp.doctorapp.dto.LoginRequest;
import com.DoctorApp.doctorapp.dto.RegisterRequest;
import com.DoctorApp.doctorapp.model.Role;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.UserRepository;
import com.DoctorApp.doctorapp.security.JwtUtil;

import java.io.IOException;
import java.nio.file.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ✅ Manual constructor (replaces Lombok)
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) throws IOException {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String imagePath = null;
        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            imagePath = uploadDir + "/" + filename;
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setImageUrl(imagePath);
        user.setSpeciality(request.getRole().equalsIgnoreCase("DOCTOR") ? request.getSpeciality() : null);


        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
            throw new RuntimeException("Role mismatch");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
