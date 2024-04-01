package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.exception.RoleNotFoundException;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import com.Team12.HADBackEnd.repository.LocalAreaRepository;
import com.Team12.HADBackEnd.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;
    @Autowired
    private LocalAreaRepository localAreaRepository;
    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<DistrictWithDoctorsDTO> getAllDistricts() {
        List<District> districts = districtRepository.findAll();
        List<DistrictWithDoctorsDTO> districtDTOs = new ArrayList<>();
        for (District district : districts) {
            DistrictWithDoctorsDTO districtDTO = new DistrictWithDoctorsDTO();
            districtDTO.setId(district.getId());
            districtDTO.setName(district.getName());

            List<DoctorDTO> doctorDTOs = new ArrayList<>();
            for (Doctor doctor : district.getDoctors()) {
                doctorDTOs.add(convertToDTO(doctor));
            }
            districtDTO.setDoctors(doctorDTOs);

            Supervisor supervisor = district.getSupervisor();
            if (supervisor != null) {
                SupervisorDTO supervisorDTO = convertToDTO(supervisor);
                districtDTO.setSupervisor(supervisorDTO);
            }

            List<LocalAreaDTO> localAreaDTOs = new ArrayList<>();
            for (LocalArea localArea : district.getLocalAreas()) {
                localAreaDTOs.add(convertToDTO(localArea));
            }
            districtDTO.setLocalAreas(localAreaDTOs);

            List<FieldHealthcareWorkerDTO> workerDTOs = new ArrayList<>();
            for (FieldHealthCareWorker worker : district.getFieldHealthCareWorkers()) {
                workerDTOs.add(convertToDTO(worker));
            }
            districtDTO.setFieldHealthCareWorkers(workerDTOs);

            districtDTOs.add(districtDTO);
        }
        return districtDTOs;
    }

    public FieldHealthcareWorkerDTO convertToDTO(FieldHealthCareWorker worker) {
        FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
        workerDTO.setId(worker.getId());
        workerDTO.setName(worker.getName());
        workerDTO.setAge(worker.getAge());
        workerDTO.setGender(worker.getGender());
        workerDTO.setEmail(worker.getEmail());
        workerDTO.setPhoneNum(worker.getPhoneNum());
        workerDTO.setUsername(worker.getUsername());
        workerDTO.setPassword(worker.getPassword());
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(worker.getDistrict().getId());
        districtDTO.setName(worker.getDistrict().getName());
        workerDTO.setDistrict(districtDTO);
        if (worker.getLocalArea() != null) {
            LocalAreaDTO localAreaDTO = new LocalAreaDTO();
            localAreaDTO.setId(worker.getLocalArea().getId());
            localAreaDTO.setName(worker.getLocalArea().getName());
            localAreaDTO.setPincode(worker.getLocalArea().getPincode());
            workerDTO.setLocalArea(localAreaDTO);
        }
        return workerDTO;
    }

    public SupervisorDTO convertToDTO(Supervisor supervisor) {
        SupervisorDTO supervisorDTO = new SupervisorDTO();
        supervisorDTO.setId(supervisor.getId());
        supervisorDTO.setName(supervisor.getName());
        supervisorDTO.setAge(supervisor.getAge());
        supervisorDTO.setGender(supervisor.getGender());
        supervisorDTO.setEmail(supervisor.getEmail());
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(supervisor.getDistrict().getId());
        districtDTO.setName(supervisor.getDistrict().getName());
        supervisorDTO.setDistrict(districtDTO);
        return supervisorDTO;
    }
    public LocalAreaDTO convertToDTO(LocalArea localArea) {
        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
        localAreaDTO.setId(localArea.getId());
        localAreaDTO.setName(localArea.getName());
        localAreaDTO.setPincode(localArea.getPincode());
        return localAreaDTO;
    }


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

    public void createDistrict(District district) {
        districtRepository.save(district);
    }
    public void createDistricts(List<District> districts) {
        districtRepository.saveAll(districts);
    }


    public String createLocalAreasInDistrict(CreateLocalAreasRequest request) {
        Long districtId = request.getDistrictId();
        List<LocalArea> localAreas = request.getLocalAreas();

        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found with id: " + districtId));

        for (LocalArea localArea : localAreas) {
            localArea.setDistrict(district);
            district.getLocalAreas().add(localArea);
            localAreaRepository.save(localArea);
        }

        districtRepository.save(district);

        return "Local areas created successfully in district with id: " + districtId;
    }


    public List<LocalAreaDTO> getAllLocalAreasByUsername(String username) {
        // Find the supervisor by username
        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new RoleNotFoundException("Supervisor not found with username: " + username));

        // Get the district from the supervisor object
        District district = supervisor.getDistrict();

        // Check if the district is null
        if (district == null) {
            throw new RoleNotFoundException("District not assigned to the supervisor with username: " + username);
        }

        // Retrieve local areas associated with the district
        List<LocalArea> localAreas = district.getLocalAreas();

        // Convert local areas to DTOs
        return localAreas.stream()
                .map(this::convertToLocalAreaDTO)
                .collect(Collectors.toList());
    }
    private LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea) {
        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
        localAreaDTO.setId(localArea.getId());
        localAreaDTO.setName(localArea.getName());
        localAreaDTO.setPincode(localArea.getPincode());
        FieldHealthCareWorker fieldHealthcareWorker = localArea.getFieldHealthCareWorker();
        if(fieldHealthcareWorker != null) {
            FieldHealthcareWorkerDTO fieldHealthcareWorkerDTO = new FieldHealthcareWorkerDTO();
            fieldHealthcareWorkerDTO.setId(fieldHealthcareWorker.getId());
            fieldHealthcareWorkerDTO.setName(fieldHealthcareWorker.getName());
            fieldHealthcareWorkerDTO.setUsername(fieldHealthcareWorker.getUsername());
            fieldHealthcareWorkerDTO.setEmail(fieldHealthcareWorker.getEmail());
            fieldHealthcareWorkerDTO.setPhoneNum(fieldHealthcareWorker.getPhoneNum());
            fieldHealthcareWorkerDTO.setAge(fieldHealthcareWorker.getAge());
            fieldHealthcareWorkerDTO.setGender(fieldHealthcareWorker.getGender());
            localAreaDTO.setFieldHealthcareWorkerDTO(fieldHealthcareWorkerDTO);
        }
        return localAreaDTO;
    }

    public List<DistrictWithoutSupervisorDTO> getAllDistrictsWithoutSupervisors() {
        List<District> districts = districtRepository.findAll();
        return districts.stream()
                .filter(district -> district.getSupervisor() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DistrictWithoutSupervisorDTO convertToDTO(District district) {
        DistrictWithoutSupervisorDTO dto = new DistrictWithoutSupervisorDTO();
        dto.setId(district.getId());
        dto.setName(district.getName());
        return dto;
    }
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