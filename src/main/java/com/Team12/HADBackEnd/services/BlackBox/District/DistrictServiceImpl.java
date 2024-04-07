package com.Team12.HADBackEnd.services.BlackBox.District;

import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService{

    private final DistrictRepository districtRepository;


    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public void createDistrict(District district) {
        districtRepository.save(district);
    }

    @Override
    public void createDistricts(List<District> districts) {
        districtRepository.saveAll(districts);
    }


    @Override
    public List<DistrictForAdminDTO> getAllDistrictsWithoutSupervisors() {
        List<District> districts = districtRepository.findAll();
        return districts.stream()
                .filter(district -> district.getSupervisor() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictForAdminDTO> getAllDistricts() {
        List<District> districts = districtRepository.findAll();
        return districts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DistrictForAdminDTO convertToDTO(District district) {
        DistrictForAdminDTO dto = new DistrictForAdminDTO();
        dto.setId(district.getId());
        dto.setName(district.getName());
        return dto;
    }
}


// @Autowired
//public FieldHealthcareWorkerDTO convertToDTO(FieldHealthCareWorker worker) {
//    FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
//    workerDTO.setId(worker.getId());
//    workerDTO.setName(worker.getName());
//    workerDTO.setAge(worker.getAge());
//    workerDTO.setGender(worker.getGender());
//    workerDTO.setEmail(worker.getEmail());
//    workerDTO.setPhoneNum(worker.getPhoneNum());
//    workerDTO.setUsername(worker.getUsername());
//    workerDTO.setPassword(worker.getPassword());
//    DistrictDTO districtDTO = new DistrictDTO();
//    districtDTO.setId(worker.getDistrict().getId());
//    districtDTO.setName(worker.getDistrict().getName());
//    workerDTO.setDistrict(districtDTO);
//    if (worker.getLocalArea() != null) {
//        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
//        localAreaDTO.setId(worker.getLocalArea().getId());
//        localAreaDTO.setName(worker.getLocalArea().getName());
//        localAreaDTO.setPincode(worker.getLocalArea().getPincode());
//        workerDTO.setLocalArea(localAreaDTO);
//    }
//    return workerDTO;
//}
//
//public SupervisorDTO convertToDTO(Supervisor supervisor) {
//    SupervisorDTO supervisorDTO = new SupervisorDTO();
//    supervisorDTO.setId(supervisor.getId());
//    supervisorDTO.setName(supervisor.getName());
//    supervisorDTO.setAge(supervisor.getAge());
//    supervisorDTO.setGender(supervisor.getGender());
//    supervisorDTO.setEmail(supervisor.getEmail());
//    DistrictDTO districtDTO = new DistrictDTO();
//    districtDTO.setId(supervisor.getDistrict().getId());
//    districtDTO.setName(supervisor.getDistrict().getName());
//    supervisorDTO.setDistrict(districtDTO);
//    return supervisorDTO;
//}
//public LocalAreaDTO convertToDTO(LocalArea localArea) {
//    LocalAreaDTO localAreaDTO = new LocalAreaDTO();
//    localAreaDTO.setId(localArea.getId());
//    localAreaDTO.setName(localArea.getName());
//    localAreaDTO.setPincode(localArea.getPincode());
//    return localAreaDTO;
//}
//
//
//private DoctorDTO convertToDTO(Doctor doctor) {
//    DoctorDTO doctorDTO = new DoctorDTO();
//    doctorDTO.setId(doctor.getId());
//    doctorDTO.setName(doctor.getName());
//    doctorDTO.setLicenseId(doctor.getLicenseId());
//    doctorDTO.setAge(doctor.getAge());
//    doctorDTO.setEmail(doctor.getEmail());
//    doctorDTO.setGender(doctor.getGender());
//    doctorDTO.setSpecialty(doctor.getSpecialty());
//    doctorDTO.setUsername(doctor.getUsername());
//    doctorDTO.setPassword(doctor.getPassword());
//    doctorDTO.setPhoneNum(doctor.getPhoneNum());
//    DistrictDTO districtDTO = new DistrictDTO();
//    districtDTO.setId(doctor.getDistrict().getId());
//    districtDTO.setName(doctor.getDistrict().getName());
//
//    doctorDTO.setDistrict(districtDTO);
//    return doctorDTO;
//}


//public List<DistrictForAdminDTO> getAllDistricts() {
//    List<District> districts = districtRepository.findAll();
//    List<DistrictWithDoctorsDTO> districtDTOs = new ArrayList<>();
//    for (District district : districts) {
//        DistrictWithDoctorsDTO districtDTO = new DistrictWithDoctorsDTO();
//        districtDTO.setId(district.getId());
//        districtDTO.setName(district.getName());
//
//        List<DoctorDTO> doctorDTOs = new ArrayList<>();
//        for (Doctor doctor : district.getDoctors()) {
//            doctorDTOs.add(convertToDTO(doctor));
//        }
//        districtDTO.setDoctors(doctorDTOs);
//
//        Supervisor supervisor = district.getSupervisor();
//        if (supervisor != null) {
//            SupervisorDTO supervisorDTO = convertToDTO(supervisor);
//            districtDTO.setSupervisor(supervisorDTO);
//        }
//
//        List<LocalAreaDTO> localAreaDTOs = new ArrayList<>();
//        for (LocalArea localArea : district.getLocalAreas()) {
//            localAreaDTOs.add(convertToDTO(localArea));
//        }
//        districtDTO.setLocalAreas(localAreaDTOs);
//
//        List<FieldHealthcareWorkerDTO> workerDTOs = new ArrayList<>();
//        for (FieldHealthCareWorker worker : district.getFieldHealthCareWorkers()) {
//            workerDTOs.add(convertToDTO(worker));
//        }
//        districtDTO.setFieldHealthCareWorkers(workerDTOs);
//
//        districtDTOs.add(districtDTO);
//    }
//    return districtDTOs;
//}