package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

}