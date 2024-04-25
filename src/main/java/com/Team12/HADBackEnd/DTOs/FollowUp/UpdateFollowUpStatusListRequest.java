package com.Team12.HADBackEnd.DTOs.FollowUp;

import java.util.List;

public class UpdateFollowUpStatusListRequest {
    private List<UpdateFollowUpStatusRequest> followUpStatusRequests;

    public List<UpdateFollowUpStatusRequest> getFollowUpStatusRequests() {
        return followUpStatusRequests;
    }

    public void setFollowUpStatusRequests(List<UpdateFollowUpStatusRequest> followUpStatusRequests) {
        this.followUpStatusRequests = followUpStatusRequests;
    }
}

