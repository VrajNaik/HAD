//package com.Team12.HADBackEnd.security.services;
//
//import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
//import com.Team12.HADBackEnd.models.Supervisor;
//import com.Team12.HADBackEnd.repository.FieldHealthCareWorkerRepository;
//import com.Team12.HADBackEnd.repository.SupervisorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Random;
//
//
//
//
//import com.Team12.HADBackEnd.models.Doctor;
//import com.Team12.HADBackEnd.models.Supervisor;
//import com.Team12.HADBackEnd.repository.DoctorRepository;
//import com.Team12.HADBackEnd.repository.SupervisorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Random;
//
//
//@Service
//public class FieldHealthCareWorkerService {
//
//    @Autowired
//    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
//
//    public FieldHealthCareWorker addFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {
//
//        // Generate a unique username
//        String generatedUsername = generateUniqueUsername();
//        fieldHealthCareWorker.setUsername(generatedUsername);
//
//        // Generate a random password
//        fieldHealthCareWorker.setPassword(generateRandomPassword());
//
//        return fieldHealthCareWorkerRepository.save(fieldHealthCareWorker);
//    }
//
//
//    public List<FieldHealthCareWorker> getAllFieldHealthCareWorker() {
//        return fieldHealthCareWorkerRepository.findAll();
//    }
//    private String generateUniqueUsername() {
//        String generatedUsername = null;
//        boolean isUnique = false;
//        while (!isUnique) {
//            // Generate a username starting with "SV" followed by a sequence of numbers
//            int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
//            generatedUsername = "FHW" + randomNumber;
//            isUnique = !fieldHealthCareWorkerRepository.existsByUsername(generatedUsername);
//        }
//        return generatedUsername;
//    }
//
//    private String generateRandomPassword() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder password = new StringBuilder();
//        Random rnd = new Random();
//        while (password.length() < 10) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * characters.length());
//            password.append(characters.charAt(index));
//        }
//        return password.toString();
//    }
//
//}
package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.repository.FieldHealthCareWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class FieldHealthCareWorkerService {

    @Autowired
    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;

    @Autowired
    private JavaMailSender javaMailSender; // Autowire the JavaMailSender

    public FieldHealthCareWorker addFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {

        // Generate a unique username
        String generatedUsername = generateUniqueUsername();
        fieldHealthCareWorker.setUsername(generatedUsername);

        // Generate a random password
        String generatedPassword = generateRandomPassword();
        fieldHealthCareWorker.setPassword(generatedPassword);

        // Save the user
        FieldHealthCareWorker savedUser = fieldHealthCareWorkerRepository.save(fieldHealthCareWorker);

        // Send email with username and password
        sendCredentialsByEmail(savedUser.getEmail(), generatedUsername, generatedPassword);

        return savedUser;
    }

    public List<FieldHealthCareWorker> getAllFieldHealthCareWorker() {
        return fieldHealthCareWorkerRepository.findAll();
    }

    private String generateUniqueUsername() {
        String generatedUsername = null;
        boolean isUnique = false;
        while (!isUnique) {
            // Generate a username starting with "FHW" followed by a sequence of numbers
            int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
            generatedUsername = "FHW" + randomNumber;
            isUnique = !fieldHealthCareWorkerRepository.existsByUsername(generatedUsername);
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