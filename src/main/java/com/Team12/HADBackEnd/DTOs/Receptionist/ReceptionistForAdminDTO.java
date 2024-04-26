package com.Team12.HADBackEnd.DTOs.Receptionist;

import com.Team12.HADBackEnd.DTOs.Hospital.HospitalDTO;


public class ReceptionistForAdminDTO {
    private String username;
    private String name;
    private String phoneNumber;
    private String email;
    private int age;
    private HospitalDTO hospitalDTO;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public HospitalDTO getHospitalDTO() {
        return hospitalDTO;
    }

    public void setHospitalDTO(HospitalDTO hospitalDTO) {
        this.hospitalDTO = hospitalDTO;
    }
}
