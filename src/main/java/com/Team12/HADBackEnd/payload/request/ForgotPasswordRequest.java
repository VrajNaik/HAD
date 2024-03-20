package com.Team12.HADBackEnd.payload.request;

public class ForgotPasswordRequest {
    private String email;

    // Constructor, getters, and setters
    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

