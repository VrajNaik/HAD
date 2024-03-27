package com.Team12.HADBackEnd.payload.request;

import java.util.Date;
import java.util.List;

public class HealthRecordCreationDTO {
    private Long id;
    private Long citizenId;
    private Long workerId;
    private Long doctorId;
    private List<Long> icd10CodeId;
    private String prescription;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
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

    public List<Long> getIcd10CodeId() {
        return icd10CodeId;
    }

    public void setIcd10CodeId(List<Long> icd10CodeId) {
        this.icd10CodeId = icd10CodeId;
    }
}

