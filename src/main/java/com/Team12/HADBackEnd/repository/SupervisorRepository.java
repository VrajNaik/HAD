package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNum(Long phoneNum);
    long count();
    Optional<Supervisor> findByUsername(String username);


    long countByActiveTrue();
}
//@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RequestMapping("/FieldHealthCareWorker")
//public class FieldHealthCareWorkerController {