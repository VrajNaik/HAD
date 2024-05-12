package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.DashICD10codes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardICD10CodeRepository extends JpaRepository<DashICD10codes, Long> {
}
