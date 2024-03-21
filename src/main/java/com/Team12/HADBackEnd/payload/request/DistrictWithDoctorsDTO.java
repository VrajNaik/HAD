package com.Team12.HADBackEnd.payload.request;

import java.util.List;

public class DistrictWithDoctorsDTO {
    private Long id;
    private String name;
    private List<DoctorDTO> doctors;

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

    public List<DoctorDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDTO> doctors) {
        this.doctors = doctors;
    }
}

