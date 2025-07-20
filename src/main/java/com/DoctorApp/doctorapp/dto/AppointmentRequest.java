package com.DoctorApp.doctorapp.dto;

import java.time.LocalDate;

public class AppointmentRequest {
    private Long doctorId;
    private String slot;
    private LocalDate date;

    // Getters and Setters
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
