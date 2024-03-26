package com.Team12.HADBackEnd.payload.response;

public class UserDeactivatedException extends RuntimeException {

    public UserDeactivatedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "{\"message\": \"" + super.getMessage() + "\"}";
    }
}
