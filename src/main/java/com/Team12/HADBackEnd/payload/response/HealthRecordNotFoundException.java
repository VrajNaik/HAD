package com.Team12.HADBackEnd.payload.response;

public class HealthRecordNotFoundException extends RuntimeException{
    public HealthRecordNotFoundException(String message) {
        super(message);
    }
}

