package com.DoctorApp.doctorapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.DoctorApp.doctorapp.model.Role;
import com.DoctorApp.doctorapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndSpecialityContainingIgnoreCase(Role role, String speciality); // ✅ Fixed
}
