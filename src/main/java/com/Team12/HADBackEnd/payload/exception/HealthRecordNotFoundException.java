package com.Team12.HADBackEnd.payload.exception;

public class HealthRecordNotFoundException extends RuntimeException{
    public HealthRecordNotFoundException(String message) {
        super(message);
    }
}

