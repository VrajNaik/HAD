package com.Team12.HADBackEnd.DTOs.FollowUp;

import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;


import java.util.Date;

public class FollowUpForDoctorDTO {
    private Long id;
    private Date date;
    private String status;
    private String instructions;
    private String measureOfVitals;
    private FieldHealthCareWorkerForAdminDTO fieldHealthCareWorker;

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

    public FieldHealthCareWorkerForAdminDTO getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorkerForAdminDTO fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
    }
}
