package com.Team12.HADBackEnd.DTOs.HealthRecord;

import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.payload.request.*;

import java.util.Date;
import java.util.List;

public class HealthRecordForDoctorDTO {
    private Long id;
    private List<PrescriptionsDTO> prescriptions;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    private String status;
    private List<FollowUpForDoctorDTO> followUps;
    private List<ICD10CodesForDoctorDTO> icd10codes;

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

    public List<FollowUpForDoctorDTO> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUpForDoctorDTO> followUps) {
        this.followUps = followUps;
    }

    public List<ICD10CodesForDoctorDTO> getIcd10codes() {
        return icd10codes;
    }

    public void setIcd10codes(List<ICD10CodesForDoctorDTO> icd10codes) {
        this.icd10codes = icd10codes;
    }
}
