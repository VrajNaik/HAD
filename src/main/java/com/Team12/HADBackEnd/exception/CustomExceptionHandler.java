package com.Team12.HADBackEnd.exception;


import com.Team12.HADBackEnd.payload.exception.CustomErrorResponse;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyActivatedException;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.RoleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RoleNotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false); // Get the URL path where the exception occurred
        CustomErrorResponse errorResponse = new CustomErrorResponse(path, "Bad Request", ex.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler({DoctorAlreadyDeactivatedException.class})
    public ResponseEntity<CustomErrorResponse> handleDoctorAlreadyDeactivatedException(DoctorAlreadyDeactivatedException ex, WebRequest request) {
        String path = request.getDescription(false); // Get the URL path where the exception occurred
        CustomErrorResponse errorResponse = new CustomErrorResponse(path, "Bad Request", ex.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler({DoctorAlreadyActivatedException.class})
    public ResponseEntity<CustomErrorResponse> handleDoctorAlreadyActivatedException(DoctorAlreadyActivatedException ex, WebRequest request) {
        String path = request.getDescription(false); // Get the URL path where the exception occurred
        CustomErrorResponse errorResponse = new CustomErrorResponse(path, "Bad Request", ex.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

