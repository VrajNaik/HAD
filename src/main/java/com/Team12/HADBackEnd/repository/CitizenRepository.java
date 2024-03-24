package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Citizen;
import com.Team12.HADBackEnd.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Citizen findByAbhaId(String abhaId);
    List<Citizen> findByDoctor(Doctor doctor);
}
