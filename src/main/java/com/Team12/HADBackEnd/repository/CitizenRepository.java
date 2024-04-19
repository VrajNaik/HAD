package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Citizen;
import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Optional<Citizen> findByAbhaId(String abhaId);

//    Citizen findByAbhaId(String abhaId);
    List<Citizen> findByDoctor(Doctor doctor);
    List<Citizen> findByStatus(String status);
    Optional<List<Citizen>> findByHospital(Hospital hospital);
}
