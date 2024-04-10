package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class FollowUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "health_record_id")
    private HealthRecord healthRecord;

    @ManyToOne
    @JoinColumn(name = "healthcare_worker_id")
    private FieldHealthCareWorker fieldHealthCareWorker;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private Date recurrenceStartTime;
    private Date recurrenceEndTime;

    @Column(name = "followup_date")
    private Date date;
    private String status;
    private String instructions;
    private String measureOfVitals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public FieldHealthCareWorker getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
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

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Date getRecurrenceStartTime() {
        return recurrenceStartTime;
    }

    public void setRecurrenceStartTime(Date recurrenceStartTime) {
        this.recurrenceStartTime = recurrenceStartTime;
    }

    public Date getRecurrenceEndTime() {
        return recurrenceEndTime;
    }

    public void setRecurrenceEndTime(Date recurrenceEndTime) {
        this.recurrenceEndTime = recurrenceEndTime;
    }
}

