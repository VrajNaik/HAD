package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseId(String licenseId);
    boolean existsByEmail(String email);
    long count();
    boolean existsByUsername(String username);
    Optional<Doctor> findByUsername(String username);
    List<Doctor> findAllByDistrictId(Long districtId);
    long countByActiveTrue();
}
