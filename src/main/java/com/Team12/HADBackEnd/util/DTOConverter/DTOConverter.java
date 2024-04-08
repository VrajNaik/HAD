package com.Team12.HADBackEnd.util.DTOConverter;

import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.models.*;
import org.springframework.stereotype.Component;

@Component
public class DTOConverter {

    public DistrictForAdminDTO convertToDistrictForAdminDTO(District district) {
        DistrictForAdminDTO dto = new DistrictForAdminDTO();
        dto.setId(district.getId());
        dto.setName(district.getName());
        return dto;
    }

    public LocalAreaForAdminDTO convertToLocalAreaForAdminDTO(LocalArea localArea) {
        LocalAreaForAdminDTO dto = new LocalAreaForAdminDTO();
        dto.setName(localArea.getName());
        dto.setPincode(localArea.getPincode());
        return dto;
    }

    public DoctorForAdminDTO convertToDoctorForAdminDTO(Doctor doctor) {
        DoctorForAdminDTO doctorDTO = new DoctorForAdminDTO();

        if (doctor.getName() != null) {
            doctorDTO.setName(doctor.getName());
        }
        if (doctor.getLicenseId() != null) {
            doctorDTO.setLicenseId(doctor.getLicenseId());
        }
        if (doctor.getAge() != 0) {
            doctorDTO.setAge(doctor.getAge());
        }
        if (doctor.getEmail() != null) {
            doctorDTO.setEmail(doctor.getEmail());
        }
        if (doctor.getGender() != null) {
            doctorDTO.setGender(doctor.getGender());
        }
        if (doctor.getSpecialty() != null) {
            doctorDTO.setSpecialty(doctor.getSpecialty());
        }
        if (doctor.getUsername() != null) {
            doctorDTO.setUsername(doctor.getUsername());
        }
        if (doctor.getPhoneNum() != null) {
            doctorDTO.setPhoneNum(doctor.getPhoneNum());
        }
        if (doctor.getDistrict() != null) {
            doctorDTO.setDistrict(convertToDistrictForAdminDTO(doctor.getDistrict()));
        }
        return doctorDTO;
    }

    public SupervisorForAdminDTO convertToSupervisorAdminDTO(Supervisor supervisor) {
        SupervisorForAdminDTO supervisorDTO = new SupervisorForAdminDTO();

        if (supervisor.getName() != null) {
            supervisorDTO.setName(supervisor.getName());
        }
        if (supervisor.getAge() != 0) {
            supervisorDTO.setAge(supervisor.getAge());
        }
        if (supervisor.getGender() != null) {
            supervisorDTO.setGender(supervisor.getGender());
        }
        if (supervisor.getEmail() != null) {
            supervisorDTO.setEmail(supervisor.getEmail());
        }
        if (supervisor.getPhoneNum() != null) {
            supervisorDTO.setPhoneNum(supervisor.getPhoneNum());
        }
        if (supervisor.getUsername() != null) {
            supervisorDTO.setUsername(supervisor.getUsername());
        }
        if (supervisor.getDistrict() != null) {
            supervisorDTO.setDistrict(convertToDistrictForAdminDTO(supervisor.getDistrict()));
        }
        return supervisorDTO;
    }

    public FieldHealthCareWorkerForAdminDTO convertToFieldHealthCareWorkerForAdminDTO(FieldHealthCareWorker worker) {
        FieldHealthCareWorkerForAdminDTO workerDTO = new FieldHealthCareWorkerForAdminDTO();

        if (worker.getName() != null) {
            workerDTO.setName(worker.getName());
        }
        if (worker.getAge() != 0) {
            workerDTO.setAge(worker.getAge());
        }
        if (worker.getGender() != null) {
            workerDTO.setGender(worker.getGender());
        }
        if (worker.getEmail() != null) {
            workerDTO.setEmail(worker.getEmail());
        }
        if (worker.getUsername() != null) {
            workerDTO.setUsername(worker.getUsername());
        }
        if (worker.getPhoneNum() != null) {
            workerDTO.setPhoneNum(worker.getPhoneNum());
        }
        if (worker.getDistrict() != null) {
            workerDTO.setDistrict(convertToDistrictForAdminDTO(worker.getDistrict()));
        }
        if (worker.getLocalArea() != null) {
            workerDTO.setLocalArea(convertToLocalAreaForAdminDTO(worker.getLocalArea()));
        }
        return workerDTO;
    }
}
