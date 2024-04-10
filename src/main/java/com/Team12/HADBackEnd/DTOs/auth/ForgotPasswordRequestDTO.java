package com.Team12.HADBackEnd.DTOs.auth;

public class ForgotPasswordRequestDTO {
    private String email;

    // Constructor, getters, and setters
    public ForgotPasswordRequestDTO() {
    }

    public ForgotPasswordRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

