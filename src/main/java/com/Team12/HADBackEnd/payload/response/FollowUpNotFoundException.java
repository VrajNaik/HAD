package com.Team12.HADBackEnd.payload.response;

public class FollowUpNotFoundException extends RuntimeException{
    public FollowUpNotFoundException(String message) {
        super(message);
    }
}
