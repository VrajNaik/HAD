package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Dashboard,Long> {

    // Custom query method to count citizens who gave consent
    long countByConsentTrue();
    long countByCity(String city);

    long countConsentByCity(String city);

    // Custom query to count citizens by follow-up status
    @Query("SELECT d.followup_status, COUNT(d) FROM Dashboard d GROUP BY d.followup_status")
    List<Object[]> countCitizensByFollowupStatus();

    @Query("SELECT d.followup_status, COUNT(d) FROM Dashboard d WHERE d.city = :city GROUP BY d.followup_status")
    List<Object[]> countCitizensByFollowupStatusAndCity(String city);

    long countByCityAndGender(String city, String gender);

    long countByGender(String gender);

    long countByCityAndAgeBetween(String city, int startAge, int endAge);

    long countByAgeBetween(int startAge, int endAge);
}
