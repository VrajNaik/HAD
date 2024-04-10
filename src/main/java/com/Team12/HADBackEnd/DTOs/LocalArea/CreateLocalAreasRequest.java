package com.Team12.HADBackEnd.DTOs.LocalArea;


import com.Team12.HADBackEnd.models.LocalArea;

import java.util.List;

public class CreateLocalAreasRequest {
    private Long districtId;
    private List<LocalArea> localAreas;

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public List<LocalArea> getLocalAreas() {
        return localAreas;
    }

    public void setLocalAreas(List<LocalArea> localAreas) {
        this.localAreas = localAreas;
    }
}
