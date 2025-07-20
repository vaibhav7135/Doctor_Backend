package com.DoctorApp.doctorapp.service;

import com.DoctorApp.doctorapp.dto.AppointmentRequest;
import com.DoctorApp.doctorapp.model.Appointment;
import com.DoctorApp.doctorapp.model.AppointmentStatus;
import com.DoctorApp.doctorapp.model.User;
import com.DoctorApp.doctorapp.repository.AppointmentRepository;
import com.DoctorApp.doctorapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    // Patient-side booking
    public void bookAppointment(AppointmentRequest request) {
        String patientEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setSlot(request.getSlot());
        appointment.setDate(request.getDate());
        appointment.setStatus(AppointmentStatus.PENDING); // default to PENDING

        appointmentRepository.save(appointment);
    }

    // Doctor-side: View all appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Doctor-side: Change appointment status
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isPresent()) {
            Appointment appointment = optional.get();
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Appointment not found with ID: " + appointmentId);
    }

    // Doctor-side: Delete appointment
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
}
