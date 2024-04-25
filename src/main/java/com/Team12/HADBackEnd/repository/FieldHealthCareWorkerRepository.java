package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FieldHealthCareWorkerRepository extends JpaRepository<FieldHealthCareWorker, Long> {

    boolean existsByUsername(String username);
    long count();
    boolean existsByEmail(String email);
    boolean existsByPhoneNum(Long phoneNum);
    Optional<FieldHealthCareWorker> findByUsername(String username);
    @Query("SELECT w FROM FieldHealthCareWorker w WHERE w.username = :username")
    FieldHealthCareWorker findUsername(String username);

    List<FieldHealthCareWorker> findByLocalAreaIsNullAndDistrictId(Long districtId);
    List<FieldHealthCareWorker> findByDistrictId(Long districtId);


    long countByActiveTrue();
}
