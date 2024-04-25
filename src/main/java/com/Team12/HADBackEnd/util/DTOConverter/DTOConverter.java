package com.Team12.HADBackEnd.util.DTOConverter;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.District.DistrictDTO;
import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICDCodesDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.response.FollowUpReturnDTO;
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

//        List<HealthRecord> healthRecords = citizen.getHealthRecord();
//        if (!healthRecords.isEmpty()) {
//            HealthRecord lastHealthRecord = healthRecords.get(healthRecords.size() - 1);
//            //citizenDTO.setHealthRecordDTO(convertToHealthRecordForDoctorDTO(lastHealthRecord));
//            HealthRecordForDoctorDTO lastHealthRecordDTO = convertToHealthRecordForDoctorDTO(lastHealthRecord);
//            List<HealthRecordForDoctorDTO> healthRecordDTOList = new ArrayList<>();
//            healthRecordDTOList.add(lastHealthRecordDTO);
//            citizenDTO.setHealthRecordDTO(healthRecordDTOList);
//        }
        List<HealthRecord> healthRecords = citizen.getHealthRecord();
        List<HealthRecordForDoctorDTO> healthRecordDTOs = new ArrayList<>();
        for (HealthRecord healthRecord : healthRecords) {
            healthRecordDTOs.add(convertToHealthRecordForDoctorDTO(healthRecord));
        }
        citizenDTO.setHealthRecordDTO(healthRecordDTOs);
        return citizenDTO;
    }

    public DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();

        doctorDTO.setId(doctor.getId());

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
        if (doctor.getPassword() != null) {
            doctorDTO.setPassword(doctor.getPassword());
        }
        if (doctor.getPhoneNum() != null) {
            doctorDTO.setPhoneNum(doctor.getPhoneNum());
        }
        if (doctor.getDistrict() != null) {
            doctorDTO.setDistrict(convertToDistrictDTO(doctor.getDistrict()));
        }

        return doctorDTO;
    }
    public DistrictDTO convertToDistrictDTO(District district) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(district.getId());
        districtDTO.setName(district.getName());
        return districtDTO;
    }
    public LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea) {
        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
        localAreaDTO.setId(localArea.getId());
        localAreaDTO.setName(localArea.getName());
        localAreaDTO.setPincode(localArea.getPincode());
        return localAreaDTO;
    }

    public FieldHealthCareWorkerWithHealthRecordDTO convertToFieldHealthCareWorkerWithHealthRecordDTO(FieldHealthCareWorker worker) {
        FieldHealthCareWorkerWithHealthRecordDTO workerDTO = new FieldHealthCareWorkerWithHealthRecordDTO();
        workerDTO.setId(worker.getId());

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
        if (worker.getPhoneNum() != null) {
            workerDTO.setPhoneNum(worker.getPhoneNum());
        }
        if (worker.getUsername() != null) {
            workerDTO.setUsername(worker.getUsername());
        }
        if (worker.getPassword() != null) {
            workerDTO.setPassword(worker.getPassword());
        }
        if (worker.getDistrict() != null) {
            workerDTO.setDistrict(convertToDistrictDTO(worker.getDistrict()));
        }
        if (worker.getLocalArea() != null) {
            workerDTO.setLocalArea(convertToLocalAreaDTO(worker.getLocalArea()));
        }
        if(worker.getHealthRecords() != null) {
            List<HealthRecordDTO> healthRecordDTOs = worker.getHealthRecords().stream()
                    .map(this::convertToHealthRecordDTO)
                    .collect(Collectors.toList());
            workerDTO.setHealthRecord(healthRecordDTOs);
        }
        return workerDTO;
    }

    public HealthRecordDTO convertToHealthRecordDTO(HealthRecord healthRecord) {
        HealthRecordDTO healthRecordDTO = new HealthRecordDTO();

        healthRecordDTO.setId(healthRecord.getId());

        if (healthRecord.getPrescriptions() != null) {
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

        if(healthRecord.getCitizen() != null) {
            healthRecordDTO.setCitizenDTO(convertToCitizenDTO(healthRecord.getCitizen()));
        }
        if(healthRecord.getDoctor() != null) {
            healthRecordDTO.setDoctorDTO(convertToDTO(healthRecord.getDoctor()));
        }

        if (healthRecord.getFollowUps() != null && !healthRecord.getFollowUps().isEmpty()) {
            List<FollowUpDTO> followUpDTOs = healthRecord.getFollowUps().stream()
                    .map(this::convertToFollowUpDTO)
                    .collect(Collectors.toList());
            healthRecordDTO.setFollowUps(followUpDTOs);
        }

        if (healthRecord.getIcd10Codes() != null && !healthRecord.getIcd10Codes().isEmpty()) {
            List<ICDCodesDTO> icd10CodesDTOs = healthRecord.getIcd10Codes().stream()
                    .map(this::convertToICDCodesDTO)
                    .collect(Collectors.toList());
            healthRecordDTO.setIcd10codes(icd10CodesDTOs);
        }

        return healthRecordDTO;
    }

    public FollowUpDTO convertToFollowUpDTO(FollowUp followUp) {
        FollowUpDTO followUpDTO = new FollowUpDTO();
        if(followUp.getId() != null) {
            followUpDTO.setId(followUp.getId());
        }
        if (followUp.getDate() != null) {
            followUpDTO.setDate(followUp.getDate());
        }
        if (followUp.getMeasureOfVitals() != null) {
            followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
        }
        if (followUp.getStatus() != null) {
            followUpDTO.setStatus(followUp.getStatus());
        }
        if (followUp.getInstructions() != null) {
            followUpDTO.setInstructions(followUp.getInstructions());
        }
        return followUpDTO;
    }

    public ICDCodesDTO convertToICDCodesDTO(ICD10Code icd10Code) {
        ICDCodesDTO icdCodesDTO = new ICDCodesDTO();
        icdCodesDTO.setId(icd10Code.getId());
        if (icd10Code.getCode() != null) {
            icdCodesDTO.setCode(icd10Code.getCode());
        }
        if (icd10Code.getDescription() != null) {
            icdCodesDTO.setDescription(icd10Code.getDescription());
        }
        if (icd10Code.getName() != null) {
            icdCodesDTO.setName(icd10Code.getName());
        }
        return icdCodesDTO;
    }

    public CitizenForAdminDTO convertToCitizenForAdminDTO(Citizen citizen) {
        CitizenForAdminDTO dto = new CitizenForAdminDTO();
        dto.setId(citizen.getId());
        if (citizen.getName() != null) {
            dto.setName(citizen.getName());
        }
        dto.setAge(citizen.getAge());
        if (citizen.getAbhaId() != null) {
            dto.setAbhaId(citizen.getAbhaId());
        }
        if (citizen.getAddress() != null) {
            dto.setAddress(citizen.getAddress());
        }
        if (citizen.getGender() != null) {
            dto.setGender(citizen.getGender());
        }
        if (citizen.getPincode() != null) {
            dto.setPincode(citizen.getPincode());
        }
        if (citizen.getState() != null) {
            dto.setState(citizen.getState());
        }
        dto.setConsent(citizen.isConsent());
        if(citizen.getStatus() != null) {
            dto.setStatus(citizen.getStatus());
        }
        if(citizen.getDistrict() != null) {
            dto.setDistrict(convertToDistrictDTO(citizen.getDistrict()));
        }
        return dto;
    }

    public CitizenDTO convertToCitizenDTO(Citizen citizen) {
        CitizenDTO dto = new CitizenDTO();
        dto.setId(citizen.getId());
        if (citizen.getName() != null) {
            dto.setName(citizen.getName());
        }
        dto.setAge(citizen.getAge());
        if (citizen.getAbhaId() != null) {
            dto.setAbhaId(citizen.getAbhaId());
        }
        if (citizen.getAddress() != null) {
            dto.setAddress(citizen.getAddress());
        }
        if (citizen.getGender() != null) {
            dto.setGender(citizen.getGender());
        }
        if (citizen.getPincode() != null) {
            dto.setPincode(citizen.getPincode());
        }
        if (citizen.getState() != null) {
            dto.setState(citizen.getState());
        }
        dto.setConsent(citizen.isConsent());
        if(citizen.getStatus() != null) {
            dto.setStatus(citizen.getStatus());
        }
        if(citizen.getDistrict() != null) {
            dto.setDistrict(citizen.getDistrict().getName());
        }
        return dto;
    }

    public FollowUpReturnDTO mapToDTO(FollowUp followUp) {
        return new FollowUpReturnDTO(
                followUp.getId(),
                followUp.getDate(),
                followUp.getStatus(),
                followUp.getInstructions()
        );
    }
}
