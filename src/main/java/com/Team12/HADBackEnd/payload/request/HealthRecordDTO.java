package com.Team12.HADBackEnd.payload.request;

import java.util.Date;

public class HealthRecordDTO {
    private Long id;
    private String prescription;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    private String status;
    private FieldHealthcareWorkerDTO fieldHealthCareWorker;
    private DoctorDTO doctorDTO;
    private CitizenDTO citizenDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FieldHealthcareWorkerDTO getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthcareWorkerDTO fieldHealthCareWorker) {
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
}
