package com.Team12.HADBackEnd.services.dashboardService;

import com.Team12.HADBackEnd.models.Dashboard;
import com.Team12.HADBackEnd.repository.DashboardICD10CodeRepository;
import com.Team12.HADBackEnd.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DashboardICD10CodeRepository dashboardICD10CodeRepository;

    public long getTotalCitizensByCity(String city) {
        return dashboardRepository.countByCity(city);
    }

    public long getTotalCitizens() {
        return dashboardRepository.count();
    }

    public long getConsentStatusByCity(String city, boolean consent) {
        return dashboardRepository.countByCityAndConsent(city, consent);
    }

    public long getConsentStatus(boolean consent) {
        return dashboardRepository.countByConsent(consent);
    }

//    public long getTotalConsentByCity(String city) {
//        return dashboardRepository.countConsentByCity(city);
//    }
//
//    public long getTotalConsent() {
//        return dashboardRepository.countByConsentTrue();
//    }

    public List<Object[]> getCitizensByFollowupStatus() {
        return dashboardRepository.countCitizensByFollowupStatus();
    }

    public Map<String, Long> getCitizensByFollowupStatusAndCity(String city) {
//        if (city != null && !city.isEmpty()) {
//            return dashboardRepository.countCitizensByFollowupStatusAndCity(city);
//        } else {
//            return dashboardRepository.countCitizensByFollowupStatus();
//        }
        Map<String, Long> followupStatusCounts = new HashMap<>();

        // Fetch counts for each follow-up status and update the map
        List<Object[]> statusCounts;
        if (city != null && !city.isEmpty()) {
            statusCounts = dashboardRepository.countCitizensByFollowupStatusAndCity(city);
        } else {
            statusCounts = dashboardRepository.countCitizensByFollowupStatus();
        }

        // Process the data and update the counts in the map
        for (Object[] row : statusCounts) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            followupStatusCounts.put(status, count);
        }

        // Ensure all possible follow-up statuses are present in the map with counts initialized to zero
        List<String> allFollowupStatuses = Arrays.asList("ongoing", "pending", "completed");
        for (String status : allFollowupStatuses) {
            followupStatusCounts.putIfAbsent(status, 0L);
        }

        return followupStatusCounts;
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



    public List<Dashboard> getTotalCitizens(String month, String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardRepository.findByMonthAndCity(month, city);
        } else {
            return dashboardRepository.findByMonth(month);
        }
    }

    public long getTotalConsent(String month, String city, boolean consent) {
        if (city != null && !city.isEmpty()) {
            return dashboardRepository.countByMonthAndCityAndConsent(month, city, consent);
        } else {
            return dashboardRepository.countByMonthAndConsent(month, consent);
        }
    }

    public List<Object[]> getCitizensByFollowupStatus(String month) {
        return dashboardRepository.countCitizensByFollowupStatus(month);
    }

//    public List<Object[]> getCitizensByFollowupStatus(String month, String city) {
//        if (city != null && !city.isEmpty()) {
//            return dashboardRepository.countCitizensByFollowupStatusAndCity(month, city);
//        } else {
//            return dashboardRepository.countCitizensByFollowupStatus(month);
//        }
//    }

    //----------------------------------------------------------------------------------//

    public Map<String, Map<String, Object>> getAllCityData() {
        Map<String, Map<String, Object>> allCityData = new HashMap<>();
        List<String> cities = getAllCities(); // Implement this method to fetch all cities from your database
        for (String city : cities) {
            Map<String, Object> cityData = new HashMap<>();
            // Fetch and add data for each city
            cityData.put("totalCitizens", dashboardRepository.countByCity(city));
            cityData.put("consentStatus", getConsentStatusByCity(city));
            cityData.put("followupStatus", getCitizensByFollowupStatusAndCity(city));
            //cityData.put("genderDistribution", getGenderDistribution(city));
            // Add more data as needed...
            allCityData.put(city, cityData);
        }
        return allCityData;
    }


   //----------------------------------------------------------------------------//
    public List<String> getAllCities() {
        return dashboardRepository.findAllCities(); // Assuming you have a method in the repository to fetch all cities
    }

    public Map<String, Long> getConsentStatusByCity(String city) {
        Map<String, Long> consentStatus = new HashMap<>();
        consentStatus.put("trueCount", dashboardRepository.countByCityAndConsent(city, true));
        consentStatus.put("falseCount", dashboardRepository.countByCityAndConsent(city, false));
        return consentStatus;
    }

    public Map<String, Long> getGenderDistribution(String city) {
        Map<String, Long> genderDistribution = new HashMap<>();
        genderDistribution.put("maleCount", dashboardRepository.countByCityAndGender(city, "Male"));
        genderDistribution.put("femaleCount", dashboardRepository.countByCityAndGender(city, "Female"));
        return genderDistribution;
    }

    //-------------------------------------------------------------------------------//
//    public Map<String, Map<String, Object>> getCityWiseAggregatedDataWithMonthWiseData() {
//        Map<String, Map<String, Object>> cityWiseAggregatedData = new HashMap<>();
//        List<String> cities = getAllCities();
//
//        for (String city : cities) {
//            Map<String, Object> cityData = new HashMap<>();
//            cityData.put("totalCitizens", dashboardRepository.countByCity(city));
//            cityData.put("consentStatus", getConsentStatusByCity(city));
//            cityData.put("genderDistribution", getGenderDistribution(city));
//            cityData.put("ageDistribution", getAgeDistribution(city));
//            // Add more data as needed...
//
//            // Fetch month-wise data for this city
//            Map<String, Map<String, Object>> monthWiseData = getMonthWiseDataForCity(city);
//            cityData.put("monthWiseData", monthWiseData);
//
//            cityWiseAggregatedData.put(city, cityData);
//        }
//
//        return cityWiseAggregatedData;
//    }
//
//    private Map<String, Map<String, Object>> getMonthWiseDataForCity(String city) {
//        Map<String, Map<String, Object>> monthWiseData = new HashMap<>();
//        List<String> allMonths = getAllMonths();
//
//        for (String month : allMonths) {
//            Map<String, Object> data = new HashMap<>();
//            List<Dashboard> totalCitizens = dashboardRepository.findByMonthAndCity(month, city);
//            data.put("totalCitizens", totalCitizens.size());
//            // Add more month-wise data as needed...
//            monthWiseData.put(month, data);
//        }
//
//        return monthWiseData;
//    }
//    public Map<String, Long> getAgeDistribution(String city) {
//        Map<String, Long> ageDistribution = new HashMap<>();
//        ageDistribution.put("13-18", getCountOfCitizensInAgeRangeByCity(city, 13, 18));
//        ageDistribution.put("31-45", getCountOfCitizensInAgeRangeByCity(city, 31, 45));
//        ageDistribution.put("46-60", getCountOfCitizensInAgeRangeByCity(city, 46, 60));
//        ageDistribution.put("61+", getCountOfCitizensInAgeRangeByCity(city, 61, Integer.MAX_VALUE));
//        return ageDistribution;
//    }



    public Map<String, Object> getCityDataWithMonthWiseData(String city) {
        Map<String, Object> cityData = new HashMap<>();
        cityData.put("totalCitizens", dashboardRepository.countByCity(city));
        cityData.put("consentStatus", getConsentStatusByCity(city));
        cityData.put("genderDistribution", getGenderDistribution(city));
        cityData.put("ageDistribution", getAgeDistribution(city));

        List<String> months = dashboardRepository.findAllMonths();
        Map<String, Map<String, Object>> monthWiseData = new HashMap<>();
        for (String month : months) {
            Map<String, Object> data = new HashMap<>();
            List<Dashboard> totalCitizens = dashboardRepository.findByMonthAndCity(month, city);
            data.put("totalCitizens", totalCitizens.size());
            data.put("consentStatusTrue", dashboardRepository.countByMonthAndCityAndConsent(month, city, true));
            data.put("consentStatusFalse", dashboardRepository.countByMonthAndCityAndConsent(month, city, false));
            data.put("followupStatus", getFollowupStatusForMonthAndCity(month, city));
            monthWiseData.put(month, data);
        }
        cityData.put("monthWiseData", monthWiseData);

        return cityData;
    }

//    private List<Object[]> getFollowupStatusForMonthAndCity(String month, String city) {
//        return dashboardRepository.countCitizensByFollowupStatusAndCity(month, city);
//    }




    public Map<String, Long> getAgeDistribution(String city) {
        Map<String, Long> ageDistribution = new HashMap<>();
        ageDistribution.put("13-18", getCountOfCitizensInAgeRangeByCity(city, 13, 18));
        ageDistribution.put("31-45", getCountOfCitizensInAgeRangeByCity(city, 31, 45));
        ageDistribution.put("46-60", getCountOfCitizensInAgeRangeByCity(city, 46, 60));
        ageDistribution.put("61+", getCountOfCitizensInAgeRangeByCity(city, 61, Integer.MAX_VALUE));
        return ageDistribution;
    }

    public List<String> getAllMonths() {
        return dashboardRepository.findAllMonths();
    }

    public List<Object[]> getCitizensByFollowupStatus(String month, String city) {
        return dashboardRepository.countCitizensByFollowupStatusAndCity(month, city);
    }

    public Map<String, Long> getFollowupStatusForMonthAndCity(String month, String city) {
        List<Object[]> followupStatusList = dashboardRepository.countCitizensByFollowupStatusAndCity(month, city);
        Map<String, Long> followupStatusCount = new HashMap<>();

        // Initialize counts for all follow-up statuses to 0
        followupStatusCount.put("pending", 0L);
        followupStatusCount.put("ongoing", 0L);
        followupStatusCount.put("completed", 0L);

        // Update counts based on retrieved data
        for (Object[] followupStatus : followupStatusList) {
            String status = (String) followupStatus[0];
            Long count = (Long) followupStatus[1];
            followupStatusCount.put(status, count);
        }

        return followupStatusCount;
    }


    //-----------------------------icd10code------------------------------------------//
//    public Map<String, Object> getDashboardData() {
//        List<Object[]> icd10Codes = dashboardICD10CodeRepository.findICD10CodeCounts();
//
//        // Calculate total count
//        int totalCount = icd10Codes.size();
//
//        // Map the result to code and count
//        Map<String, Long> countMap = icd10Codes.stream()
//                .collect(Collectors.toMap(obj -> (String) obj[0], obj -> (Long) obj[1]));
//
//        // Sort by count in descending order
//        List<Map.Entry<String, Long>> sortedCounts = countMap.entrySet()
//                .stream()
//                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
//                .collect(Collectors.toList());
//
//        // Extract top 3 and calculate count of "Other"
//        List<Map.Entry<String, Long>> top3ICD10Codes = sortedCounts.stream().limit(3).collect(Collectors.toList());
//        int otherCount = sortedCounts.stream().skip(3).mapToInt(entry -> entry.getValue().intValue()).sum();
//
//        // Create response map
//        Map<String, Object> response = new HashMap<>();
//        response.put("totalICD10Codes", totalCount);
//        response.put("top3ICD10Codes", top3ICD10Codes);
//        response.put("otherCount", otherCount);
//
//        return response;
//    }

}
