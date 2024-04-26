package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.FollowUp;
import com.Team12.HADBackEnd.models.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    Optional<List<FollowUp>> findByHealthRecordId(Long healthRecordId);
    Optional<List<FollowUp>> findByFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker);
    Optional<List<FollowUp>> findByFieldHealthCareWorkerAndStatus(FieldHealthCareWorker fieldHealthCareWorker, String status);
    List<FollowUp> findByHealthRecordAndStatus(HealthRecord healthRecord, String status);
    ArrayList<FollowUp> findByStatus(String status);
}