package com.Team12.HADBackEnd.DTOs.auth;

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

public class AuthResponseDTO {
    private JwtResponseDTO jwtResponseDTO;
    private Object userRole;


    public AuthResponseDTO(JwtResponseDTO jwtResponseDTO, Object userRole) {
        this.jwtResponseDTO = jwtResponseDTO;
        this.userRole = userRole;
    }

    public JwtResponseDTO getJwtResponse() {
        return jwtResponseDTO;
    }

    public void setJwtResponse(JwtResponseDTO jwtResponseDTO) {
        this.jwtResponseDTO = jwtResponseDTO;
    }

    public Object getUserRole() {
        return userRole;
    }

    public void setUserRole(Object userRole) {
        this.userRole = userRole;
    }
}
