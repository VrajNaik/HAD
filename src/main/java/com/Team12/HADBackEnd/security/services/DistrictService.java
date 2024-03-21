package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.District;
import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.payload.request.DistrictDTO;
import com.Team12.HADBackEnd.payload.request.DistrictWithDoctorsDTO;
import com.Team12.HADBackEnd.payload.request.DoctorDTO;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

//    public List<District> getAllDistricts() {
//        return districtRepository.findAll();
//    }
public List<DistrictWithDoctorsDTO> getAllDistricts() {
    List<District> districts = districtRepository.findAll();
    List<DistrictWithDoctorsDTO> districtDTOs = new ArrayList<>();
    for (District district : districts) {
        DistrictWithDoctorsDTO districtDTO = new DistrictWithDoctorsDTO();
        districtDTO.setId(district.getId());
        districtDTO.setName(district.getName());
        // Set the list of doctors assigned to this district
        List<DoctorDTO> doctorDTOs = new ArrayList<>();
        for (Doctor doctor : district.getDoctors()) {
            doctorDTOs.add(convertToDTO(doctor));
        }
        districtDTO.setDoctors(doctorDTOs);
        districtDTOs.add(districtDTO);
    }
    return districtDTOs;
}

    // Convert Doctor entity to DoctorDTO
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setLicenseId(doctor.getLicenseId());
        doctorDTO.setAge(doctor.getAge());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setGender(doctor.getGender());
        doctorDTO.setSpecialty(doctor.getSpecialty());
        doctorDTO.setUsername(doctor.getUsername());
        doctorDTO.setPassword(doctor.getPassword());
        doctorDTO.setPhoneNum(doctor.getPhoneNum());
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(doctor.getDistrict().getId());
        districtDTO.setName(doctor.getDistrict().getName());

        doctorDTO.setDistrict(districtDTO);
        return doctorDTO;
    }

//    public List<District> getUnallocatedDistricts() {
//        return districtRepository.findBySupervisorIsNull();
//    }

    public void createDistrict(District district) {
        districtRepository.save(district);
    }
    public void createDistricts(List<District> districts) {
        districtRepository.saveAll(districts);
    }
}
