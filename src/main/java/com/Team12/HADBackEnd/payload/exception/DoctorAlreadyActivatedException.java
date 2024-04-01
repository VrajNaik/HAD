package com.Team12.HADBackEnd.payload.exception;

public class DoctorAlreadyActivatedException extends RuntimeException {
    public DoctorAlreadyActivatedException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "{\"message\": \"" + super.getMessage() + "\"}";
    }
}
