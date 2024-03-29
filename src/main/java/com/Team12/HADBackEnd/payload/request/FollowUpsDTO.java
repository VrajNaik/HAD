package com.Team12.HADBackEnd.payload.request;

import java.util.Date;

public class FollowUpsDTO {
    private Long id;
    private Date date;
    private String status;
    private String instructions;
    private String measureOfVitals;
    private Long healthWorkerId;
    private String healthWorkerName;

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

    public Long getHealthWorkerId() {
        return healthWorkerId;
    }

    public void setHealthWorkerId(Long healthWorkerId) {
        this.healthWorkerId = healthWorkerId;
    }

    public String getHealthWorkerName() {
        return healthWorkerName;
    }

    public void setHealthWorkerName(String healthWorkerName) {
        this.healthWorkerName = healthWorkerName;
    }
}
