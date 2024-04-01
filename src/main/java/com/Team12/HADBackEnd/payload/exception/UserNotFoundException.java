package com.Team12.HADBackEnd.payload.exception;


import java.util.HashMap;
import java.util.Map;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
//    @Override
//    public String getMessage() {
//        return "{\n" +
//                "    \"message\": \"" + super.getMessage() + "\"\n" +
//                "}";
//    }

    public Map<String, Object> getErrorResponse() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", super.getMessage());
        return jsonMap;
    }
}
