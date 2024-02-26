package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseId(String licenseId);

    boolean existsByUsername(String username);
}
