package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNum);
    boolean existsByUHID(String UHID);
    Optional<List<Hospital>> findByDistrictId(Long districtId);
    Optional<Hospital> findByUHID(String UHID);
}
