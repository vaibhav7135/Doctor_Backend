package com.DoctorApp.doctorapp.controller;

import com.DoctorApp.doctorapp.model.Role;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.UserRepository;
import com.DoctorApp.doctorapp.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;
    private final UserRepository userRepository;

    public DoctorController(DoctorService doctorService, UserRepository userRepository) {
        this.doctorService = doctorService;
        this.userRepository = userRepository;
    }

    // ✅ This handles both general and filtered queries
    @GetMapping
    public ResponseEntity<List<User>> getDoctors(@RequestParam(required = false) String speciality) {
        List<User> doctors;
        if (speciality != null && !speciality.isEmpty()) {
        	doctors = userRepository.findByRoleAndSpecialityContainingIgnoreCase(Role.DOCTOR, speciality);

        } else {
            doctors = doctorService.getAllDoctors();
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
}
