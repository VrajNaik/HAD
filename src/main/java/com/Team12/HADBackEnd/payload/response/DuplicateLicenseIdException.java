package com.Team12.HADBackEnd.payload.response;

public class DuplicateLicenseIdException extends RuntimeException {
    public DuplicateLicenseIdException(String message) {
        super(message);
    }
}
