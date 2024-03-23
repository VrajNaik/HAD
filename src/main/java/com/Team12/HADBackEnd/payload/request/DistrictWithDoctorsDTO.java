package com.Team12.HADBackEnd.payload.request;

import java.util.List;

public class DistrictWithDoctorsDTO {
    private Long id;
    private String name;
    private List<DoctorDTO> doctors;
    private SupervisorDTO supervisor;
    private List<LocalAreaDTO> localAreas;
    private List<FieldHealthcareWorkerDTO> fieldHealthCareWorkers;

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

    public List<LocalAreaDTO> getLocalAreas() {
        return localAreas;
    }

    public void setLocalAreas(List<LocalAreaDTO> localAreas) {
        this.localAreas = localAreas;
    }

    public SupervisorDTO getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDTO supervisor) {
        this.supervisor = supervisor;
    }

    public List<FieldHealthcareWorkerDTO> getFieldHealthCareWorkers() {
        return fieldHealthCareWorkers;
    }

    public void setFieldHealthCareWorkers(List<FieldHealthcareWorkerDTO> fieldHealthCareWorkers) {
        this.fieldHealthCareWorkers = fieldHealthCareWorkers;
    }
}

