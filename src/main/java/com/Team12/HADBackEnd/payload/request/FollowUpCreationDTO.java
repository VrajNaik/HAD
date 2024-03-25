package com.Team12.HADBackEnd.payload.request;

import java.util.Date;

public class FollowUpCreationDTO {

    private Date date;
    private String status;
    private String instructions;
    private String measureOfVitals;

    private Long healthRecordId;
    private Long fieldHealthCareWorkerId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMeasureOfVitals() {
        return measureOfVitals;
    }

    public void setMeasureOfVitals(String measureOfVitals) {
        this.measureOfVitals = measureOfVitals;
    }

    public Long getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(Long healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public Long getFieldHealthCareWorkerId() {
        return fieldHealthCareWorkerId;
    }

    public void setFieldHealthCareWorkerId(Long fieldHealthCareWorkerId) {
        this.fieldHealthCareWorkerId = fieldHealthCareWorkerId;
    }
}
