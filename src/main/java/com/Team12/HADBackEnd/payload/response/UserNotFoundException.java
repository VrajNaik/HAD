package com.Team12.HADBackEnd.payload.response;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "{\"message\": \"" + super.getMessage() + "\"}";
    }
}
