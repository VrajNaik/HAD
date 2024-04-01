package com.Team12.HADBackEnd.payload.exception;


import java.util.HashMap;
import java.util.Map;

public class DoctorAlreadyDeactivatedException extends RuntimeException {
    public DoctorAlreadyDeactivatedException(String message) {
        super(message);
    }
//    @Override
//    public String getMessage() {
//        return "{\"message\": \"" + super.getMessage() + "\"}";
//    }
    public Map<String, Object> getErrorResponse() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", super.getMessage());
        return jsonMap;
    }
}


