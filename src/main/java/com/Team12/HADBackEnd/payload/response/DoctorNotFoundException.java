package com.Team12.HADBackEnd.payload.response;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "{\"message\": \"" + super.getMessage() + "\"}";
    }
}

