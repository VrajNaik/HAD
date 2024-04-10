package com.Team12.HADBackEnd.DTOs.FollowUp;

public class UpdateFollowUpStatusRequest {
    private Long followUpId;
    private String status;

    public Long getFollowUpId() {
        return followUpId;
    }

    public void setFollowUpId(Long followUpId) {
        this.followUpId = followUpId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
