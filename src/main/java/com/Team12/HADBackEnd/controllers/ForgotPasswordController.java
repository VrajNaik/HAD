package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.request.ForgotPasswordRequest;
import com.Team12.HADBackEnd.payload.request.ResetPasswordRequest;
import com.Team12.HADBackEnd.payload.response.CustomErrorResponse;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//
//@RestController
//@RequestMapping("/api")
//public class ForgotPasswordController {
//
//    @Autowired
//    private ForgotPasswordService forgotPasswordService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
//        // Check if user exists
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            // User not found, return error response
//            return ResponseEntity.badRequest().body("User with email " + email + " not found.");
//        }
//
//        // User found, initiate password reset
//        forgotPasswordService.initiatePasswordReset(email);
//        return ResponseEntity.ok("Password reset instructions sent to your email.");
//    }
//
//    @PostMapping("/reset-password")
//
//    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
//        boolean success = forgotPasswordService.resetPassword(token, newPassword);
//        if (success) {
//            return ResponseEntity.ok("Password reset successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Invalid or expired token.");
//        }
//    }
//}
//
//@RestController
//@RequestMapping("/api")
//public class ForgotPasswordController {
//
//    @Autowired
//    private ForgotPasswordService forgotPasswordService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
//        String email = forgotPasswordRequest.getEmail();
//        // Check if user exists
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            // User not found, return error response
//            return ResponseEntity.badRequest().body("User with email " + email + " not found.");
//        }
//
//        // User found, initiate password reset
//        forgotPasswordService.initiatePasswordReset(email);
//        return ResponseEntity.ok("Password reset instructions sent to your email.");
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
//        String token = resetPasswordRequest.getToken();
//        String newPassword = resetPasswordRequest.getNewPassword();
//        boolean success = forgotPasswordService.resetPassword(token, newPassword);
//        if (success) {
//            return ResponseEntity.ok("Password reset successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Invalid or expired token.");
//        }
//    }
//}

@RestController
@RequestMapping("/api")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        // Check if user exists
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // User not found, return error response
            CustomErrorResponse errorResponse = new CustomErrorResponse("/api/forgot-password", "User Not Found", "User with email " + email + " not found.", 404);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // User found, initiate password reset
        forgotPasswordService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset instructions sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String token = resetPasswordRequest.getToken();
        String newPassword = resetPasswordRequest.getNewPassword();
        boolean success = forgotPasswordService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            // Password reset failed, return error response
            CustomErrorResponse errorResponse = new CustomErrorResponse("/api/reset-password", "Invalid Token", "Invalid or expired token.", 400);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

