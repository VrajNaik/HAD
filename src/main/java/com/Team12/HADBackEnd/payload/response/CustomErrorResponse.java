package com.Team12.HADBackEnd.payload.response;

public class CustomErrorResponse {
    private String path;
    private String error;
    private String message;
    private int status;

    // Constructor, getters, and setters
    public CustomErrorResponse() {
    }

    public CustomErrorResponse(String path, String error, String message, int status) {
        this.path = path;
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
