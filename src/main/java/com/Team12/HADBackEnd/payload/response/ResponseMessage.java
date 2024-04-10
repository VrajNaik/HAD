package com.Team12.HADBackEnd.payload.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ResponseMessage {
    public static ResponseEntity<Object> createSuccessResponse(HttpStatus status, String message) {
        String path = generatePath();
        return ResponseEntity.status(status).body(
                new ApiResponse(status.value(), path, message)
        );
    }

    public static ResponseEntity<Object> createNotSuccessResponse(HttpStatus status, String message) {
        String path = generatePath();
        return ResponseEntity.status(status).body(
                new ApiResponse(status.value(), path, message)
        );
    }

    static class ApiResponse {
        private final int status;
        private final String path;
        private final String message;

        public ApiResponse(int status, String path, String message) {
            this.status = status;
            this.path = path;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getPath() {
            return path;
        }

        public String getMessage() {
            return message;
        }
    }

    private static String generatePath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getRequestURI();
    }
}

