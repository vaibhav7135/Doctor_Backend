package com.DoctorApp.doctorapp.service;

import com.DoctorApp.doctorapp.model.Role;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final UserRepository userRepository;

    // ✅ Manual constructor
    public DoctorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    public User getDoctorById(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getRole() == Role.DOCTOR)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
}
