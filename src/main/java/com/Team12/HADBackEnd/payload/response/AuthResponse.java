package com.Team12.HADBackEnd.payload.response;

//
//import java.util.Map;
//
//public class AuthResponse {
//    private JwtResponse jwtResponse;
//    private Map<String, Long> counts;
//
//
//    public AuthResponse(JwtResponse jwtResponse,Map<String, Long> counts) {
//        this.jwtResponse = jwtResponse;
//        this.counts = counts;
//    }
//
//    // Getters and setters
//    public JwtResponse getJwtResponse() {
//        return jwtResponse;
//    }
//
//    public void setJwtResponse(JwtResponse jwtResponse) {
//        this.jwtResponse = jwtResponse;
//    }
//
//    public Map<String, Long> getCounts() {
//        return counts;
//    }
//
//    public void setCounts(Map<String, Long> counts) {
//        this.counts = counts;
//    }
//
//
//}

import java.util.Map;

public class AuthResponse {
    private JwtResponse jwtResponse;
    private Map<String, Long> counts;


    public AuthResponse(JwtResponse jwtResponse, Map<String, Long> counts) {
        this.jwtResponse = jwtResponse;
        this.counts = counts;
    }

    // Getters and setters
    public JwtResponse getJwtResponse() {
        return jwtResponse;
    }

    public void setJwtResponse(JwtResponse jwtResponse) {
        this.jwtResponse = jwtResponse;
    }

    public Map<String, Long> getCounts() {
        return counts;
    }

    public void setCounts(Map<String, Long> counts) {
        this.counts = counts;
    }
}
