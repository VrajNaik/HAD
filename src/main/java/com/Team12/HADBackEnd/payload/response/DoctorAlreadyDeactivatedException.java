package com.Team12.HADBackEnd.payload.response;


public class DoctorAlreadyDeactivatedException extends RuntimeException {
    public DoctorAlreadyDeactivatedException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "{\"message\": \"" + super.getMessage() + "\"}";
    }
}


