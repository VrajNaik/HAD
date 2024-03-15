package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseId(String licenseId);
    boolean existsByEmail(String email);
    long count();
    boolean existsByUsername(String username);
}
