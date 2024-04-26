package com.Team12.HADBackEnd.DTOs.FollowUp;

import java.util.List;

public class UpdateFollowUpStatusListRequest {
    private List<UpdateFollowUpStatusRequest> followUpInstructionsRequests;

    public List<UpdateFollowUpStatusRequest> getFollowUpInstructionsRequests() {
        return followUpInstructionsRequests;
    }

    public void setFollowUpInstructionsRequests(List<UpdateFollowUpStatusRequest> followUpInstructionsRequests) {
        this.followUpInstructionsRequests = followUpInstructionsRequests;
    }
}