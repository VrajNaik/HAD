package com.Team12.HADBackEnd.DTOs.HealthRecord;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICDCodesDTO;

import java.util.Date;
import java.util.List;

public class HealthRecordDTO {
    private Long id;
    private List<PrescriptionsDTO> prescriptions;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    private String status;
    private FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorker;
    private DoctorDTO doctorDTO;
    private CitizenDTO citizenDTO;
    private List<FollowUpDTO> followUps;
    private List<ICDCodesDTO> icd10codes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PrescriptionsDTO> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionsDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FieldHealthCareWorkerWithHealthRecordDTO getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
    }

    public DoctorDTO getDoctorDTO() {
        return doctorDTO;
    }

    public void setDoctorDTO(DoctorDTO doctorDTO) {
        this.doctorDTO = doctorDTO;
    }

    public CitizenDTO getCitizenDTO() {
        return citizenDTO;
    }

    public void setCitizenDTO(CitizenDTO citizenDTO) {
        this.citizenDTO = citizenDTO;
    }

    public List<FollowUpDTO> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUpDTO> followUps) {
        this.followUps = followUps;
    }

    public List<ICDCodesDTO> getIcd10codes() {
        return icd10codes;
    }

    public void setIcd10codes(List<ICDCodesDTO> icd10codes) {
        this.icd10codes = icd10codes;
    }
}
