package com.Team12.HADBackEnd.payload.request;


import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;

import java.util.List;

public class FieldHealthcareWorkerDTO {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private Long phoneNum;
    private String username;
    private String password;
    private DistrictDTO district;
    private LocalAreaDTO localArea;
    private List<HealthRecordDTO> healthRecord;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    public LocalAreaDTO getLocalArea() {
        return localArea;
    }

    public void setLocalArea(LocalAreaDTO localAreaDTO) {
        this.localArea = localAreaDTO;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<HealthRecordDTO> getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(List<HealthRecordDTO> healthRecord) {
        this.healthRecord = healthRecord;
    }
}


