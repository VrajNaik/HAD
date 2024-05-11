package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.services.dashboardService.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*", origins = "*")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/totalCitizens")
    public long getTotalCitizens(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardService.getTotalCitizensByCity(city);
        } else {
            return dashboardService.getTotalCitizens();
        }
    }
//    public long getTotalCitizens() {
//        return dashboardService.getTotalCitizens();
//    }

    @GetMapping("/totalConsent")
    public long getTotalConsent(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardService.getTotalConsentByCity(city);
        } else {
            return dashboardService.getTotalConsent();
        }
    }
    public long getTotalConsent() {
        return dashboardService.getTotalConsent();
    }

    @GetMapping("/citizensByFollowupStatus")
    public List<Object[]> getCitizensByFollowupStatus(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardService.getCitizensByFollowupStatusAndCity(city);
        } else {
            return dashboardService.getCitizensByFollowupStatus();
        }
    }

    @GetMapping("/genderDistribution")
    public Map<String, Long> getGenderDistribution(@RequestParam(required = false) String city) {
        Map<String, Long> genderDistribution = new HashMap<>();
        if (city != null && !city.isEmpty()) {
            genderDistribution.put("maleCount", dashboardService.getTotalMaleCitizensByCity(city));
            genderDistribution.put("femaleCount", dashboardService.getTotalFemaleCitizensByCity(city));
        } else {
            genderDistribution.put("totalMaleCount", dashboardService.getTotalMaleCitizens());
            genderDistribution.put("totalFemaleCount", dashboardService.getTotalFemaleCitizens());
        }
        return genderDistribution;
    }

    @GetMapping("/ageDistribution")
    public Map<String, Long> getAgeDistribution(@RequestParam(required = false) String city) {
        Map<String, Long> ageDistribution = new HashMap<>();
        if (city != null && !city.isEmpty()) {
            ageDistribution.put("13-18", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 13, 18));
            ageDistribution.put("31-45", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 31, 45));
            ageDistribution.put("46-60", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 46, 60));
            ageDistribution.put("61+", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 61, Integer.MAX_VALUE));
        } else {
            ageDistribution.put("13-18", dashboardService.getCountOfCitizensInAgeRange(13, 18));
            ageDistribution.put("31-45", dashboardService.getCountOfCitizensInAgeRange(31, 45));
            ageDistribution.put("46-60", dashboardService.getCountOfCitizensInAgeRange(46, 60));
            ageDistribution.put("61+", dashboardService.getCountOfCitizensInAgeRange(61, Integer.MAX_VALUE));
        }
        return ageDistribution;
    }
}
