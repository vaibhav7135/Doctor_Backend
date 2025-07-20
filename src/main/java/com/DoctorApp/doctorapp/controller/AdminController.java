package com.DoctorApp.doctorapp.controller;

import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.model.Appointment;
import com.DoctorApp.doctorapp.repository.UserRepository;
import com.DoctorApp.doctorapp.repository.AppointmentRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AdminController(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/doctors")
    public List<User> getAllDoctors() {
        return userRepository.findByRole(com.DoctorApp.doctorapp.model.Role.DOCTOR);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/doctors/{id}")
    public User getDoctorById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @DeleteMapping("/appointments/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
    }

}
