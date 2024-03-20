package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findBySupervisorIsNull();
}
