package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.repository.DoctorRepository;
import com.Team12.HADBackEnd.repository.RoleRepository;
import com.Team12.HADBackEnd.repository.SupervisorRepository;
import com.Team12.HADBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender; // Autowire the JavaMailSender

    public Supervisor addSupervisor(Supervisor supervisor) {
        String generatedUsername = generateUniqueUsername();
        String generatedRandomPassword = generateRandomPassword();

        // paste here
        supervisor.setUsername(generatedUsername);

        // Generate a random password
        supervisor.setPassword(generatedRandomPassword);
        sendCredentialsByEmail(supervisor.getEmail(), generatedUsername, generatedRandomPassword);

        return supervisorRepository.save(supervisor);
    }

    public List<Supervisor> getAllSupervisor() {
        return supervisorRepository.findAll();
    }
    private String generateUniqueUsername() {
        String generatedUsername = null;
        boolean isUnique = false;
        while (!isUnique) {
            // Generate a username starting with "SV" followed by a sequence of numbers
            int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
            generatedUsername = "SV" + randomNumber;
            isUnique = !supervisorRepository.existsByUsername(generatedUsername);
        }
        return generatedUsername;
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        while (password.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
    private void sendCredentialsByEmail(String email, String username, String password) {
        // Prepare email message
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Credentials for accessing the system");
        mailMessage.setText("Your username: " + username + "\nYour password: " + password);

        // Send email
        javaMailSender.send(mailMessage);
    }
}

//// Create new user's account
//User user = new User(generatedUsername,
//        supervisor.getEmail(),
//        encoder.encode(generatedRandomPassword));
//
//Set<String> strRoles = new HashSet<>();
//        strRoles.add("supervisor");
//Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//        .orElseThrow(() -> new RuntimeException("Error: DOCTOR is not found."));
//            roles.add(userRole);
//        } else {
//                strRoles.forEach(role -> {
//        switch (role) {
//        case "admin":
//Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                                case "supervisor":
//Role modRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
//        .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR is not found."));
//                        roles.add(modRole);
//
//                        break;
//default:
//Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//                        });
//                        }
//
//                        user.setRoles(roles);
//        userRepository.save(user);



//random

//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class AuthEntryPointJwt implements AuthenticationEntryPoint {
//
//  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
//
//  @Override
//  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//      throws IOException, ServletException {
//    logger.error("Unauthorized error: {}", authException.getMessage());
//
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//    final Map<String, Object> body = new HashMap<>();
//    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//    body.put("error", "Unauthorized");
//    body.put("message", authException.getMessage());
//    body.put("path", request.getServletPath());
//
//    final ObjectMapper mapper = new ObjectMapper();
//    mapper.writeValue(response.getOutputStream(), body);
//  }
//
//}