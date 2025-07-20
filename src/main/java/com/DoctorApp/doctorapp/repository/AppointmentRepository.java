package com.DoctorApp.doctorapp.repository;


import com.DoctorApp.doctorapp.model.Appointment;
import com.DoctorApp.doctorapp.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByDoctorId(Long doctorId);
	List<Appointment> findByPatient(User patient);



}
