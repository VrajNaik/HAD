package com.Team12.HADBackEnd.DTOs.HealthRecord;

import com.Team12.HADBackEnd.models.Prescription;

import java.util.Date;
import java.util.List;

public class HealthRecordCreationDTO {
    private Long id;
    private String abhaId;
    private String workerUsername;
    private String doctorUsername;
    private List<Long> icd10CodeId;
    private List<PrescriptionForHealthRecordDTO> prescription;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }

    public String getWorkerUsername() {
        return workerUsername;
    }

    public void setWorkerUsername(String workerUsername) {
        this.workerUsername = workerUsername;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
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

    public List<PrescriptionForHealthRecordDTO> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<PrescriptionForHealthRecordDTO> prescription) {
        this.prescription = prescription;
    }
}

