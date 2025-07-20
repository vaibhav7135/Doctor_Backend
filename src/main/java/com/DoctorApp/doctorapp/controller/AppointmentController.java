package com.DoctorApp.doctorapp.controller;

import com.DoctorApp.doctorapp.dto.AppointmentRequest;
import com.DoctorApp.doctorapp.model.Appointment;
import com.DoctorApp.doctorapp.model.AppointmentStatus;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.AppointmentRepository;
import com.DoctorApp.doctorapp.repository.UserRepository;
import com.DoctorApp.doctorapp.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserRepository userRepository,AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.appointmentRepository=appointmentRepository;
    }

    // ✅ Book appointment (patient side)
    @PostMapping
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentRequest request) {
        appointmentService.bookAppointment(request);
        return ResponseEntity.ok("Appointment booked successfully ✅");
    }

    // ✅ Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> testAccess() {
        return ResponseEntity.ok("Appointments OK ✅");
    }

    // ✅ Get all appointments for logged-in doctor
    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getDoctorAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctor.getId());
        return ResponseEntity.ok(appointments);
    }

    // ✅ Admin: Get all appointments
    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // ✅ Doctor: Update appointment status (approve/cancel)
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        Appointment updated = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // ✅ Doctor/Admin: Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully.");
    }
    
 // Get appointments for patient
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getPatientAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return ResponseEntity.ok(appointments);
    }

}
