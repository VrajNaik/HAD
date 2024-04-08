package com.Team12.HADBackEnd.util.DTOConverter;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.models.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private FollowUpForDoctorDTO convertToDTO(FollowUp followUp) {
        FollowUpForDoctorDTO followUpDTO = new FollowUpForDoctorDTO();
        followUpDTO.setId(followUp.getId());
        if (followUp.getDate() != null) {
            followUpDTO.setDate(followUp.getDate());
        }
        if (followUp.getStatus() != null) {
            followUpDTO.setStatus(followUp.getStatus());
        }
        if (followUp.getInstructions() != null) {
            followUpDTO.setInstructions(followUp.getInstructions());
        }
        if(followUp.getMeasureOfVitals() != null) {
            followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
        }
        FieldHealthCareWorker fieldHealthCareWorker = followUp.getFieldHealthCareWorker();
        if (fieldHealthCareWorker != null) {
            followUpDTO.setFieldHealthCareWorker(convertToFieldHealthCareWorkerForAdminDTO(fieldHealthCareWorker));
        }
        return followUpDTO;
    }

    public HealthRecordForDoctorDTO convertToHealthRecordForDoctorDTO(HealthRecord healthRecord) {
        HealthRecordForDoctorDTO healthRecordDTO = new HealthRecordForDoctorDTO();
        healthRecordDTO.setId(healthRecord.getId());
        if(healthRecord.getPrescriptions() != null) {
            healthRecordDTO.setPrescriptions(healthRecord.getPrescriptions());
        }
        if (healthRecord.getConclusion() != null) {
            healthRecordDTO.setConclusion(healthRecord.getConclusion());
        }
        if (healthRecord.getDiagnosis() != null) {
            healthRecordDTO.setDiagnosis(healthRecord.getDiagnosis());
        }
        if (healthRecord.getTimestamp() != null) {
            healthRecordDTO.setTimestamp(healthRecord.getTimestamp());
        }
        if (healthRecord.getStatus() != null) {
            healthRecordDTO.setStatus(healthRecord.getStatus());
        }
        if (healthRecord.getIcd10Codes() != null && !healthRecord.getIcd10Codes().isEmpty()) {
            healthRecordDTO.setIcd10codes(convertToICD10CodesForDoctorDTO(healthRecord.getIcd10Codes()));
        }
        if (healthRecord.getFollowUps() != null && !healthRecord.getFollowUps().isEmpty()) {
            List<FollowUpForDoctorDTO> followUpsDTO = healthRecord.getFollowUps().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            healthRecordDTO.setFollowUps(followUpsDTO);
        }
        return healthRecordDTO;
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

    public List<ICD10CodesForDoctorDTO> convertToICD10CodesForDoctorDTO(List<ICD10Code> icd10Codes) {
        List<ICD10CodesForDoctorDTO> dtos = new ArrayList<>();
        for (ICD10Code icd10Code : icd10Codes) {
            dtos.add(convertToICD10CodesForDoctorDTO(icd10Code));
        }
        return dtos;
    }
    public ICD10CodesForDoctorDTO convertToICD10CodesForDoctorDTO(ICD10Code icd10Code) {
        return new ICD10CodesForDoctorDTO(icd10Code.getCode(), icd10Code.getName(), icd10Code.getDescription());
    }

    public CitizenForDoctorDTO convertToCitizenForDoctorDTO(Citizen citizen) {
        CitizenForDoctorDTO citizenDTO = new CitizenForDoctorDTO();
        citizenDTO.setId(citizen.getId());
        if (citizen.getName() != null) {
            citizenDTO.setName(citizen.getName());
        }
        if (citizen.getAge() != 0) {
            citizenDTO.setAge(citizen.getAge());
        }
        if (citizen.getGender() != null) {
            citizenDTO.setGender(citizen.getGender());
        }
        if (citizen.getAddress() != null) {
            citizenDTO.setAddress(citizen.getAddress());
        }
        citizenDTO.setConsent(citizen.isConsent());
        if (citizen.getPincode() != null) {
            citizenDTO.setPincode(citizen.getPincode());
        }
        if (citizen.getState() != null) {
            citizenDTO.setState(citizen.getState());
        }
        if (citizen.getStatus() != null) {
            citizenDTO.setStatus(citizen.getStatus());
        }
        if (citizen.getDistrict() != null) {
            citizenDTO.setDistrict(citizen.getDistrict().getName());
        }
        if (citizen.getAbhaId() != null) {
            citizenDTO.setAbhaId(citizen.getAbhaId());
        }
        FieldHealthCareWorker fieldHealthCareWorker = citizen.getFieldHealthCareWorker();
        if (fieldHealthCareWorker != null) {
            citizenDTO.setFieldHealthCareWorker(convertToFieldHealthCareWorkerForAdminDTO(fieldHealthCareWorker));
        }

        HealthRecord healthRecord = citizen.getHealthRecord();
        if (healthRecord != null) {
            citizenDTO.setHealthRecordDTO(convertToHealthRecordForDoctorDTO(healthRecord));
        }
        return citizenDTO;
    }
}
