package com.Team12.HADBackEnd.DTOs.FollowUp;

import com.Team12.HADBackEnd.models.Frequency;

import java.util.Date;

public class FollowUpCreationByDoctorDTO {
    private Long healthRecordId;
    private String workerUsername;
    private Date scheduledDateTime;
    private Frequency frequency;
    private String instructions;
    private Date recurrenceEndTime;

    public Long getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(Long healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public String getWorkerUsername() {
        return workerUsername;
    }

    public void setWorkerUsername(String workerUsername) {
        this.workerUsername = workerUsername;
    }

    public Date getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(Date scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Date getRecurrenceEndTime() {
        return recurrenceEndTime;
    }

    public void setRecurrenceEndTime(Date recurrenceEndTime) {
        this.recurrenceEndTime = recurrenceEndTime;
    }
}