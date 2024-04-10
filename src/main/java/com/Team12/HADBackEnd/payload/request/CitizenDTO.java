package com.Team12.HADBackEnd.payload.request;

import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CitizenDTO {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String address;
    private boolean consent;
    private String pincode;
    private String status;
    private String state;
    private String district;
    private String abhaId;
    private FieldHealthcareWorkerDTO fieldHealthCareWorker;
    private DoctorDTO doctorDTO;
    private HealthRecordDTO healthRecordDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
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

    public HealthRecordDTO getHealthRecordDTO() {
        return healthRecordDTO;
    }

    public void setHealthRecordDTO(HealthRecordDTO healthRecordDTO) {
        this.healthRecordDTO = healthRecordDTO;
    }
}

