package com.Team12.HADBackEnd.payload.exception;

public class DuplicateLicenseIdException extends RuntimeException {
    public DuplicateLicenseIdException(String message) {
        super(message);
    }
}