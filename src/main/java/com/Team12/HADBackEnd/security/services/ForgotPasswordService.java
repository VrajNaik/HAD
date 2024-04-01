package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.RoleNotFoundException;
import com.Team12.HADBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    @Autowired
    private SupervisorRepository supervisorRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            userRepository.save(user);
            System.out.println("Reset Token for " + email + ": " + token);
            sendResetPasswordEmail(user);
        }
    }

    private void sendResetPasswordEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText("To reset your password, please click the following link: http://yourapp.com/reset-password?token=" + user.getResetToken());
        javaMailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null && user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(encoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RoleNotFoundException("User not found with username: " + username));
        String userTypePrefix = username.substring(0, 2);

        if (user != null && user.isLogInFirst() == false) {
            String password = encoder.encode(newPassword);
            switch (userTypePrefix) {
                case "FH":
                    // Fetch user from FieldHealthCareWorker repository
                    FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findByUsername(username)
                            .orElseThrow(() -> new RoleNotFoundException("Field Health Care Worker not found with username: " + username));
                    fieldHealthCareWorker.setPassword(password);
                    break;
                // Add cases for other user types if needed
                case "SV":
                    // Fetch user from FieldHealthCareWorker repository
                    Supervisor supervisor = supervisorRepository.findByUsername(username)
                            .orElseThrow(() -> new RoleNotFoundException("Supervisor not found with username: " + username));
                    supervisor.setPassword(password);
                    break;
                case "DR":
                    // Fetch user from FieldHealthCareWorker repository
                    Doctor doctor = doctorRepository.findByUsername(username)
                            .orElseThrow(() -> new RoleNotFoundException("Supervisor not found with username: " + username));
                    doctor.setPassword(password);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid user type prefix in username: " + userTypePrefix);
            }
            user.setPassword(password);
            user.setLogInFirst(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}

