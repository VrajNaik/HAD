package com.Team12.HADBackEnd.payload.response;

public class DuplicateEmailIdException extends RuntimeException {
    public DuplicateEmailIdException(String message) {
        super(message);
    }
}
