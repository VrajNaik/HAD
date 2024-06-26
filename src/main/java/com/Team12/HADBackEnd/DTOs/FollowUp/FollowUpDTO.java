package com.Team12.HADBackEnd.DTOs.FollowUp;

import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;

import java.util.Date;

public class FollowUpDTO {
    private Long id;
    private Date date;
    private String status;
    private String instructions;
    private String measureOfVitals;
    private FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorker;
    private HealthRecordDTO healthRecord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public FieldHealthCareWorkerWithHealthRecordDTO getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
    }

    public HealthRecordDTO getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecordDTO healthRecord) {
        this.healthRecord = healthRecord;
    }
}
