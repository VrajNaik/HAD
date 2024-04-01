package com.Team12.HADBackEnd;

import com.Team12.HADBackEnd.payload.exception.FollowUpNotFoundException;
import com.Team12.HADBackEnd.payload.exception.HealthRecordNotFoundException;
import com.Team12.HADBackEnd.payload.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(RoleNotFoundException.class)
//    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex) {
//        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
//    }
    @ExceptionHandler(FollowUpNotFoundException.class)
    public ResponseEntity<?> handleFollowUpNotFoundException(FollowUpNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
    @ExceptionHandler(HealthRecordNotFoundException.class)
    public ResponseEntity<?> handleHealthRecordNotFoundException(HealthRecordNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
