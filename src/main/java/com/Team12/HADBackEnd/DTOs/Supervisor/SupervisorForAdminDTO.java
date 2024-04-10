package com.Team12.HADBackEnd.DTOs.Supervisor;

import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;

public class SupervisorForAdminDTO {
    private String name;

    private int age;

    private String gender;

    private String email;

    private Long phoneNum;

    private String username;

    private DistrictForAdminDTO district;

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

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DistrictForAdminDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictForAdminDTO district) {
        this.district = district;
    }
}
