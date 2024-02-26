package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    boolean existsByUsername(String username);
    long count();
}
