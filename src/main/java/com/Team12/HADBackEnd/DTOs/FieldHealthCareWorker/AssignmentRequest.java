package com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker;

public class AssignmentRequest {
    private String username;
    private Long localAreaId;

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getLocalAreaId() {
        return localAreaId;
    }

    public void setLocalAreaId(Long localAreaId) {
        this.localAreaId = localAreaId;
    }
}
