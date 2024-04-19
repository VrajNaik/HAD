package com.Team12.HADBackEnd.DTOs.Hospital;

import com.Team12.HADBackEnd.DTOs.District.DistrictDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Receptionist.ReceptionistDTO;

import java.util.List;

public class HospitalDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private int numberOfBeds;
    private DistrictDTO district;
    private List<DoctorForAdminDTO> doctors;
    private ReceptionistDTO receptionist;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    public List<DoctorForAdminDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorForAdminDTO> doctors) {
        this.doctors = doctors;
    }

    public ReceptionistDTO getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(ReceptionistDTO receptionist) {
        this.receptionist = receptionist;
    }
}

