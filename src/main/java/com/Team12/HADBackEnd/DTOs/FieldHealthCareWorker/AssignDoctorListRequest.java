package com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker;

import java.util.List;

public class AssignDoctorListRequest {
    private List<AssignDoctorRequest> doctorAssignments;

    public List<AssignDoctorRequest> getDoctorAssignments() {
        return doctorAssignments;
    }

    public void setDoctorAssignments(List<AssignDoctorRequest> doctorAssignments) {
        this.doctorAssignments = doctorAssignments;
    }
}

