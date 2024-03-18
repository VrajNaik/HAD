package com.Team12.HADBackEnd.payload.response;

public class DoctorAlreadyActivatedException extends RuntimeException {
    public DoctorAlreadyActivatedException(String message) {
        super(message);
    }
}
