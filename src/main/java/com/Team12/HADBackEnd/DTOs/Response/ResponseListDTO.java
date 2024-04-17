package com.Team12.HADBackEnd.DTOs.Response;

import java.util.List;

public class ResponseListDTO {
    private List<ResponseDTO> responses;

    public List<ResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseDTO> responses) {
        this.responses = responses;
    }
}
