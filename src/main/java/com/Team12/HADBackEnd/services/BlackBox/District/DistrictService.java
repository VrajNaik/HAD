package com.Team12.HADBackEnd.services.BlackBox.District;
import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.models.*;


import java.util.List;

public interface DistrictService {

    void createDistrict(District district);

    void createDistricts(List<District> districts);

    List<DistrictForAdminDTO> getAllDistrictsWithoutSupervisors();

    List<DistrictForAdminDTO> getAllDistricts();

}

//    public List<District> getAllDistricts() {
//        return districtRepository.findAll();
//    }

//    public List<District> getUnallocatedDistricts() {
//        return districtRepository.findBySupervisorIsNull();
//    }


//    public List<DistrictWithDoctorsDTO> getAllDistricts() {
//        List<District> districts = districtRepository.findAll();
//        List<DistrictWithDoctorsDTO> districtDTOs = new ArrayList<>();
//        for (District district : districts) {
//            DistrictWithDoctorsDTO districtDTO = new DistrictWithDoctorsDTO();
//            districtDTO.setId(district.getId());
//            districtDTO.setName(district.getName());
//            // Set the list of doctors assigned to this district
//            List<DoctorDTO> doctorDTOs = new ArrayList<>();
//            for (Doctor doctor : district.getDoctors()) {
//                doctorDTOs.add(convertToDTO(doctor));
//            }
//            districtDTO.setDoctors(doctorDTOs);
//            districtDTOs.add(districtDTO);
//        }
//        return districtDTOs;
//    }

//    public List<LocalAreaDTO> getAllLocalAreasByDistrictId(Long districtId) {
//        District district = districtRepository.findById(districtId)
//                .orElseThrow(() -> new RuntimeException("District not found with id: " + districtId));
//
//        return district.getLocalAreas().stream()
//                .map(this::convertToLocalAreaDTO)
//                .collect(Collectors.toList());
//    }