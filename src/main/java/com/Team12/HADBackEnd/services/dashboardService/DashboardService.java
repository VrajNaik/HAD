package com.Team12.HADBackEnd.services.dashboardService;

import com.Team12.HADBackEnd.models.Dashboard;
import com.Team12.HADBackEnd.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Object[]> getCitizensByFollowupStatus(String month, String city) {
        if (city != null && !city.isEmpty()) {
            return dashboardRepository.countCitizensByFollowupStatusAndCity(month, city);
        } else {
            return dashboardRepository.countCitizensByFollowupStatus(month);
        }
    }

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
    
}
