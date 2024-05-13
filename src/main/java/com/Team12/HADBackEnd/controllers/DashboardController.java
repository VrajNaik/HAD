package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Dashboard;
import com.Team12.HADBackEnd.services.dashboardService.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

//    @GetMapping("/totalConsent")
//    public long getTotalConsent(@RequestParam(required = false) String city) {
//        if (city != null && !city.isEmpty()) {
//            return dashboardService.getTotalConsentByCity(city);
//        } else {
//            return dashboardService.getTotalConsent();
//        }
//    }
//    public long getTotalConsent() {
//        return dashboardService.getTotalConsent();
//    }

    @GetMapping("/consentStatus")
    public Map<String, Long> getConsentStatus(@RequestParam(required = false) String city) {
        Map<String, Long> consentCounts = new HashMap<>();
        if (city != null && !city.isEmpty()) {
            consentCounts.put("trueCount", dashboardService.getConsentStatusByCity(city, true));
            consentCounts.put("falseCount", dashboardService.getConsentStatusByCity(city, false));
        } else {
            consentCounts.put("trueCount", dashboardService.getConsentStatus(true));
            consentCounts.put("falseCount", dashboardService.getConsentStatus(false));
        }
        return consentCounts;
    }

    @GetMapping("/citizensByFollowupStatus")
    public List<Object[]> getCitizensByFollowupStatus(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardService.getFollowupStatusAndCity(city);
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
            ageDistribution.put("19-30", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 19, 30));
            ageDistribution.put("31-45", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 31, 45));
            ageDistribution.put("46-60", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 46, 60));
            ageDistribution.put("61+", dashboardService.getCountOfCitizensInAgeRangeByCity(city, 61, Integer.MAX_VALUE));
        } else {
            ageDistribution.put("13-18", dashboardService.getCountOfCitizensInAgeRange(13, 18));
            ageDistribution.put("19-30", dashboardService.getCountOfCitizensInAgeRange(19, 30));
            ageDistribution.put("31-45", dashboardService.getCountOfCitizensInAgeRange(31, 45));
            ageDistribution.put("46-60", dashboardService.getCountOfCitizensInAgeRange(46, 60));
            ageDistribution.put("61+", dashboardService.getCountOfCitizensInAgeRange(61, Integer.MAX_VALUE));
        }
        return ageDistribution;
    }



//    @GetMapping("/monthwiseData")
//    public Map<String, Object> getMonthWiseData(@RequestParam String month, @RequestParam(required = false) String city) {
//        Map<String, Object> data = new HashMap<>();
//
//        // Total citizens
//        List<Dashboard> totalCitizens;
//        if (city != null && !city.isEmpty()) {
//            totalCitizens = dashboardService.getTotalCitizensByMonthAndCity(month, city);
//        } else {
//            totalCitizens = dashboardService.getTotalCitizensByMonth(month);
//        }
//        data.put("totalCitizens", totalCitizens.size());
//
//        // Consent status
//        long consentTrueCount = dashboardService.getTotalConsentByMonth(month, true);
//        long consentFalseCount = dashboardService.getTotalConsentByMonth(month, false);
//        Map<String, Long> consentStatus = new HashMap<>();
//        consentStatus.put("trueCount", consentTrueCount);
//        consentStatus.put("falseCount", consentFalseCount);
//        data.put("consentStatus", consentStatus);
//
//        // Followup status
//        List<Object[]> followupStatus = dashboardService.getCitizensByFollowupStatus(month);
//        data.put("followupStatus", followupStatus);
//
//        return data;
//    }



//    @GetMapping("/monthwiseData")
//    public Map<String, Object> getMonthWiseData(@RequestParam String month, @RequestParam(required = false) String city) {
//        Map<String, Object> data = new HashMap<>();
//
//        // Total citizens
//        List<Dashboard> totalCitizens = dashboardService.getTotalCitizens(month, city);
//        data.put("totalCitizens", totalCitizens.size());
//
//        // Consent status
//        long consentTrueCount = dashboardService.getTotalConsent(month, city, true);
//        long consentFalseCount = dashboardService.getTotalConsent(month, city, false);
//        Map<String, Long> consentStatus = new HashMap<>();
//        consentStatus.put("trueCount", consentTrueCount);
//        consentStatus.put("falseCount", consentFalseCount);
//        data.put("consentStatus", consentStatus);
//
//        List<Object[]> followupStatus;
//        if (city != null && !city.isEmpty()) {
//            followupStatus = dashboardService.getCitizensByFollowupStatus(month, city);
//        } else {
//            followupStatus = dashboardService.getCitizensByFollowupStatus(month);
//        }
//        data.put("followupStatus", followupStatus);
//
//        return data;
//    }

    //new city wise data
    @GetMapping("/cityWiseData")
    public Map<String, Map<String, Object>> getCityWiseData() {
        return dashboardService.getAllCityData();
    }

//    @GetMapping("/cityAggregatedData")
//    public Map<String, Object> getCityAggregatedData(@RequestParam String city) {
//        return dashboardService.getCityAggregatedData(city);
//    }

    @GetMapping("/cityData")
    public Map<String, Object> getCityDataWithMonthWiseData(@RequestParam String city) {
        return dashboardService.getCityDataWithMonthWiseData(city);
    }

    @GetMapping("/monthwiseData")
    public Map<String, Object> getMonthWiseData(@RequestParam(required = false) String city) {
        Map<String, Object> data = new HashMap<>();
        List<String> allMonths = dashboardService.getAllMonths();

        for (String month : allMonths) {
            Map<String, Object> monthData = new HashMap<>();
            // Total citizens
            List<Dashboard> totalCitizens = dashboardService.getTotalCitizens(month, city);
            monthData.put("totalCitizens", totalCitizens.size());

            // Consent status
            long consentTrueCount = dashboardService.getTotalConsent(month, city, true);
            long consentFalseCount = dashboardService.getTotalConsent(month, city, false);
            Map<String, Long> consentStatus = new HashMap<>();
            consentStatus.put("trueCount", consentTrueCount);
            consentStatus.put("falseCount", consentFalseCount);
            monthData.put("consentStatus", consentStatus);

            // Follow-up status
            List<Object[]> followupStatus;
            if (city != null && !city.isEmpty()) {
                followupStatus = dashboardService.getCitizensByFollowupStatus(month, city);
            } else {
                followupStatus = dashboardService.getCitizensByFollowupStatus(month);
            }
            // Creating a map to store follow-up status counts
            Map<String, Long> followupStatusMap = new HashMap<>();
            for (Object[] status : followupStatus) {
                String followup = (String) status[0];
                long count = (Long) status[1];
                followupStatusMap.put(followup, count);
            }
            // Including all possible statuses with counts even if zero
            List<String> allFollowupStatus = Arrays.asList("ongoing", "completed", "pending");
            for (String status : allFollowupStatus) {
                followupStatusMap.putIfAbsent(status, 0L);
            }
            monthData.put("followupStatus", followupStatusMap);

            data.put(month, monthData);
        }

        return data;
    }

//    @GetMapping("/icd10-codes")
//    public Map<String, Object> getDashboardData() {
//        return dashboardService.getDashboardData();
//    }

//    @GetMapping("/icd10-codes")
//    public Map<String, Integer> getTop3ICD10Codes() {
//        return dashboardService.getTop3ICD10Codes();
//    }

//    @GetMapping("/icd10-codes")
//    public Map<String, Long> getICD10CodeCounts() {
//        return dashboardService.getICD10CodeCounts();
//    }

    @GetMapping("/icd10-codes")
    public Map<String, Long> getICD10CodeCounts(@RequestParam(required = false) String city) {
        return dashboardService.getICD10CodeCounts(city);
    }



}
