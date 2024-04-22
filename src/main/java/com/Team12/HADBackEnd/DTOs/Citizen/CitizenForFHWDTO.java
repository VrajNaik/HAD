package com.Team12.HADBackEnd.DTOs.Citizen;

import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordForDoctorDTO;
import com.Team12.HADBackEnd.payload.response.FollowUpReturnDTO;

import java.util.List;

public class CitizenForFHWDTO {
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
    private List<FollowUpReturnDTO> followUpReturn;
    private HealthRecordForDoctorDTO healthRecordForDoctor;

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

    public List<FollowUpReturnDTO> getFollowUpReturn() {
        return followUpReturn;
    }

    public void setFollowUpReturn(List<FollowUpReturnDTO> followUpReturn) {
        this.followUpReturn = followUpReturn;
    }

    public HealthRecordForDoctorDTO getHealthRecordForDoctor() {
        return healthRecordForDoctor;
    }

    public void setHealthRecordForDoctor(HealthRecordForDoctorDTO healthRecordForDoctor) {
        this.healthRecordForDoctor = healthRecordForDoctor;
    }
}
