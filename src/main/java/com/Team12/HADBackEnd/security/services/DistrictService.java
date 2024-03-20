package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.District;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public List<District> getUnallocatedDistricts() {
        return districtRepository.findBySupervisorIsNull();
    }

    public void createDistrict(District district) {
        districtRepository.save(district);
    }
    public void createDistricts(List<District> districts) {
        districtRepository.saveAll(districts);
    }
}
