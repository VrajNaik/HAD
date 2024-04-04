package com.Team12.HADBackEnd.payload.response;

import java.util.Date;

public class FollowUpReturnDTO {
    private Long id;
    private Date date;
    private String status;
    private String instructions;

    // Constructors, getters, and setters

    public FollowUpReturnDTO(Long id, Date date, String status, String instructions) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.instructions = instructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}

