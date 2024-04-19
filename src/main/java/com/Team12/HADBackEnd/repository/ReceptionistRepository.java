package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Hospital;
import com.Team12.HADBackEnd.models.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String  phoneNum);
    long countByActiveTrue();
    Optional<Receptionist> findByUsername(String username);
}
