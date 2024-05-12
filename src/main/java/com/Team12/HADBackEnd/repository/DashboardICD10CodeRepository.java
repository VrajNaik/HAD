package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.DashICD10codes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashboardICD10CodeRepository extends JpaRepository<DashICD10codes, Long> {

    @Query("SELECT d.code, COUNT(d) FROM DashICD10codes d GROUP BY d.code")
    List<Object[]> findICD10CodeCounts();

//    @Query("SELECT ic.code, COUNT(d) FROM Dashboard d JOIN d.dashICD10codes ic GROUP BY ic.code")
//    List<Object[]> findTopICD10CodesCounts();
}
