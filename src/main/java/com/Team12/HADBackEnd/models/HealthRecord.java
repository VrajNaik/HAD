package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private FieldHealthCareWorker fieldHealthCareWorker;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @OneToMany(mappedBy = "healthRecord")
    private List<FollowUp> followUps;

    private List<String> prescriptions;
    private String conclusion;
    private String diagnosis;
    private Date timestamp;
    private String symptoms;
    private String status = "new";
    @Lob
    private String notes;
    @ManyToMany
    @JoinTable(name = "health_record_icd10_codes",
            joinColumns = @JoinColumn(name = "health_record_id"),
            inverseJoinColumns = @JoinColumn(name = "icd10_code_id"))
    private List<ICD10Code> icd10Codes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public FieldHealthCareWorker getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public List<String> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<String> prescriptions) {
        this.prescriptions = prescriptions;
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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<FollowUp> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUp> followUps) {
        this.followUps = followUps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ICD10Code> getIcd10Codes() {
        return icd10Codes;
    }

    public void setIcd10Codes(List<ICD10Code> icd10Codes) {
        this.icd10Codes = icd10Codes;
    }
    public void addICD10Code(ICD10Code icd10Code) {
        if (icd10Codes == null) {
            icd10Codes = new ArrayList<>();
        }
        icd10Codes.add(icd10Code);
    }
}

