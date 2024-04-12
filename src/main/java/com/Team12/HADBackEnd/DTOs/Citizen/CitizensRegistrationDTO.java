package com.Team12.HADBackEnd.DTOs.Citizen;

import java.util.List;

public class CitizensRegistrationDTO {
    List<CitizenRegistrationDTO> citizens;

    public List<CitizenRegistrationDTO> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<CitizenRegistrationDTO> citizens) {
        this.citizens = citizens;
    }
}
