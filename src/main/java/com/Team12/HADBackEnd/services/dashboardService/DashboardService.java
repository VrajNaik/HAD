package com.Team12.HADBackEnd.services.dashboardService;

import com.Team12.HADBackEnd.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public long getTotalCitizensByCity(String city) {
        return dashboardRepository.countByCity(city);
    }

    public long getTotalCitizens() {
        return dashboardRepository.count();
    }

    public long getTotalConsentByCity(String city) {
        return dashboardRepository.countConsentByCity(city);
    }

    public long getTotalConsent() {
        return dashboardRepository.countByConsentTrue();
    }

    public List<Object[]> getCitizensByFollowupStatus() {
        return dashboardRepository.countCitizensByFollowupStatus();
    }

    public List<Object[]> getCitizensByFollowupStatusAndCity(String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardRepository.countCitizensByFollowupStatusAndCity(city);
        } else {
            return dashboardRepository.countCitizensByFollowupStatus();
        }
    }

    public long getTotalMaleCitizensByCity(String city) {
        return dashboardRepository.countByCityAndGender(city, "Male");
    }

    public long getTotalFemaleCitizensByCity(String city) {
        return dashboardRepository.countByCityAndGender(city, "Female");
    }

    public long getTotalMaleCitizens() {
        return dashboardRepository.countByGender("Male");
    }

    public long getTotalFemaleCitizens() {
        return dashboardRepository.countByGender("Female");
    }

    public long getCountOfCitizensInAgeRangeByCity(String city, int startAge, int endAge) {
        return dashboardRepository.countByCityAndAgeBetween(city, startAge, endAge);
    }

    public long getCountOfCitizensInAgeRange(int startAge, int endAge) {
        return dashboardRepository.countByAgeBetween(startAge, endAge);
    }
}
