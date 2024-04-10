package com.Team12.HADBackEnd.DTOs.Doctor;

import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;

public class DoctorForAdminDTO {
    private String name;
    private String licenseId;
    private int age;
    private String gender;
    private String specialty;
    private Long phoneNum;
    private String email;
    private String username;
    private boolean active = true;
    private DistrictForAdminDTO district;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DistrictForAdminDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictForAdminDTO district) {
        this.district = district;
    }
}
