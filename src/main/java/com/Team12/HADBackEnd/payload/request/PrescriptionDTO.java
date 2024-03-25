package com.Team12.HADBackEnd.payload.request;
public class PrescriptionDTO {
    private Long healthRecordId;
    private String prescription;

    public Long getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(Long healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}