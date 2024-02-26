package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FieldHealthCareWorkerRepository extends JpaRepository<FieldHealthCareWorker, Long> {

    boolean existsByUsername(String username);
    long count();
}
