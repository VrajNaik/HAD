package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    Optional<List<FollowUp>> findByHealthRecordId(Long healthRecordId);
}