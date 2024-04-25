package com.Team12.HADBackEnd.payload.response;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForFHWDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;

import java.util.Date;

public class FollowUpReturnDTO {
    private Long id;
    private Long citizenId;
    private Date date;
    private String status;
    private String instructions;
    private DoctorForAdminDTO doctor;
    private CitizenForDoctorDTO citizen;

    // Constructors, getters, and setters

    public FollowUpReturnDTO(Long id, Long citizenId, Date date, String status, String instructions, DoctorForAdminDTO doctor, CitizenForDoctorDTO citizen) {
        this.id = id;
        this.date = date;
        this.citizenId = citizenId;
        this.status = status;
        this.instructions = instructions;
        this.doctor = doctor;
        this.citizen = citizen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
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

    public DoctorForAdminDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorForAdminDTO doctor) {
        this.doctor = doctor;
    }

    public CitizenForDoctorDTO getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenForDoctorDTO citizen) {
        this.citizen = citizen;
    }
}

