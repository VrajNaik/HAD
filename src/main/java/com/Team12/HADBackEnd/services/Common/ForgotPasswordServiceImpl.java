package com.Team12.HADBackEnd.services.Common;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    private final SupervisorRepository supervisorRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder encoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public ForgotPasswordServiceImpl(UserRepository userRepository,
                                 FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                                 SupervisorRepository supervisorRepository,
                                 DoctorRepository doctorRepository,
                                 ReceptionistRepository receptionistRepository,
                                 PasswordEncoder passwordEncoder,
                                 JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.fieldHealthCareWorkerRepository = fieldHealthCareWorkerRepository;
        this.supervisorRepository = supervisorRepository;
        this.doctorRepository = doctorRepository;
        this.receptionistRepository = receptionistRepository;
        this.encoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
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

    @Override
    public void sendResetPasswordEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText("To reset your password, please click the following link: https://yourapp.com/reset-password?token=" + user.getResetToken());
        javaMailSender.send(message);
    }

    @Override
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

    @Override
    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        user.setLogInFirst(false);
        String userTypePrefix = username.substring(0, 2);

        if (user != null && !user.isLogInFirst()) {
            String password = encoder.encode(newPassword);
            switch (userTypePrefix) {
                case "FH":
                    FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findByUsername(username)
                            .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));
                    fieldHealthCareWorker.setPassword(password);
                    break;
                case "SV":
                    Supervisor supervisor = supervisorRepository.findByUsername(username)
                            .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));
                    supervisor.setPassword(password);
                    break;
                case "DR":
                    Doctor doctor = doctorRepository.findByUsername(username)
                            .orElseThrow(() -> new NotFoundException("Doctor not found with username: " + username));
                    doctor.setPassword(password);
                    break;
                case "RE":
                    Receptionist receptionist = receptionistRepository.findByUsername(username)
                            .orElseThrow(() -> new NotFoundException("Receptionist not found with username: " + username));
                    receptionist.setPassword(password);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid user type prefix in username: " + userTypePrefix);
            }
            user.setPassword(password);
            user.setLogInFirst(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
