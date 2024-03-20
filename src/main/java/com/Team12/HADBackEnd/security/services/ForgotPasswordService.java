package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.repository.UserRepository;
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
}

