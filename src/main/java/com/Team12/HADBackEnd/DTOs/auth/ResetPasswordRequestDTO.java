package com.Team12.HADBackEnd.DTOs.auth;

public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;

    // Constructor, getters, and setters
    public ResetPasswordRequestDTO() {
    }

    public ResetPasswordRequestDTO(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

