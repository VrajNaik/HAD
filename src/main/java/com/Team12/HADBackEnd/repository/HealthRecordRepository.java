package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    Optional<HealthRecord> findByCitizen_Id(Long citizenId);


//    HealthRecord findByCitizen_Id(Long citizenId);
}
