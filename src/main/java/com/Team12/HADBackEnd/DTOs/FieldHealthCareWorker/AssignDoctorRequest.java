package com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker;

public class AssignDoctorRequest {
    private String abhaId;
    private String doctorUsername;

    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }
}

