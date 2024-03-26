package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.request.DistrictDTO;
import com.Team12.HADBackEnd.payload.request.SupervisorDTO;
import com.Team12.HADBackEnd.payload.request.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.payload.response.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.response.DoctorNotFoundException;
import com.Team12.HADBackEnd.payload.response.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.response.UserNotFoundException;
import com.Team12.HADBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldHealthCareWorkerRepository workerRepository;
    @Autowired
    private LocalAreaRepository localAreaRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JavaMailSender javaMailSender; // Autowire the JavaMailSender

    @Transactional(rollbackFor = Exception.class)
    public Supervisor addSupervisor(Supervisor supervisor) throws DuplicateEmailIdException {
        String generatedUsername = generateUniqueUsername();
        String generatedRandomPassword = generateRandomPassword();

        // Retrieve the associated District object
        District district = supervisor.getDistrict();

        // Create new user's account
        User user = new User(generatedUsername,
                supervisor.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role supervisorRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR role not found."));
        roles.add(supervisorRole);
        user.setRoles(roles);
        userRepository.save(user);

        // Check for duplicate email
        if (supervisorRepository.existsByEmail(supervisor.getEmail())) {
            throw new DuplicateEmailIdException("Supervisor with the same Email ID already exists.");
        }

        // Set supervisor's details
        supervisor.setUsername(generatedUsername);
        supervisor.setPassword(encoder.encode(generatedRandomPassword));
        supervisor.setDistrict(district);

        // Save supervisor
        Supervisor savedSupervisor = supervisorRepository.save(supervisor);

        // Send email with username and password
        sendCredentialsByEmail(savedSupervisor.getEmail(), generatedUsername, generatedRandomPassword);

        return savedSupervisor;
    }
    public List<SupervisorDTO> getAllSupervisorsWithDistricts() {
        List<Supervisor> supervisors = supervisorRepository.findAll();
        return supervisors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional(rollbackFor = Exception.class)
    public SupervisorDTO updateSupervisor(SupervisorUpdateRequestDTO request) {
        Supervisor supervisor = supervisorRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Supervisor not found with id: " + request.getId()));

        if (request.getName() != null) {
            supervisor.setName(request.getName());
        }
        if (request.getAge() != 0) {
            supervisor.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            supervisor.setGender(request.getGender());
        }
        if (request.getEmail() != null) {
            supervisor.setEmail(request.getEmail());
        }
        if (request.getNewDistrictId() != null) {
            District newDistrict = districtRepository.findById(request.getNewDistrictId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getNewDistrictId()));
            supervisor.setDistrict(newDistrict);
        }

        Supervisor updatedSupervisor = supervisorRepository.save(supervisor);
        return convertToDTO(updatedSupervisor);
    }

    private SupervisorDTO convertToDTO(Supervisor supervisor) {
        SupervisorDTO supervisorDTO = new SupervisorDTO();
        supervisorDTO.setId(supervisor.getId());

        if (supervisor.getName() != null) {
            supervisorDTO.setName(supervisor.getName());
        }
        if (supervisor.getAge() != 0) {
            supervisorDTO.setAge(supervisor.getAge());
        }
        if (supervisor.getGender() != null) {
            supervisorDTO.setGender(supervisor.getGender());
        }
        if (supervisor.getEmail() != null) {
            supervisorDTO.setEmail(supervisor.getEmail());
        }
        if (supervisor.getUsername() != null) {
            supervisorDTO.setUsername(supervisor.getUsername());
        }
        if (supervisor.getPassword() != null) {
            supervisorDTO.setPassword(supervisor.getPassword());
        }
        if (supervisor.getDistrict() != null) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setId(supervisor.getDistrict().getId());
            districtDTO.setName(supervisor.getDistrict().getName());
            supervisorDTO.setDistrict(districtDTO);
        }

        return supervisorDTO;
    }
    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {
        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException("Supervisor not found with username: " + username));

        if (supervisor.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Supervisor is already " + (active ? "activated" : "deactivated"));
        }

        supervisor.setActive(active);
        if (!active) {
            supervisor.setDistrict(null);
        }
        supervisorRepository.save(supervisor);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        user.setActive(active);
        userRepository.save(user);
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
    public String assignWorkerToLocalArea(String username, Long localAreaId) {
        FieldHealthCareWorker worker = workerRepository.findUsername(username);
        if (worker == null) {
            return "Worker not found";
        }

        LocalArea localArea = localAreaRepository.findById(localAreaId).orElse(null);
        if (localArea == null) {
            return "Local area not found";
        }

        worker.setLocalArea(localArea);
        workerRepository.save(worker);

        return "Worker assigned successfully";
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
//    public Supervisor addSupervisor(Supervisor supervisor) {
//        String generatedUsername = generateUniqueUsername();
//        String generatedRandomPassword = generateRandomPassword();
//
//        // paste here
//        supervisor.setUsername(generatedUsername);
//
//        // Generate a random password
//        supervisor.setPassword(generatedRandomPassword);
//        sendCredentialsByEmail(supervisor.getEmail(), generatedUsername, generatedRandomPassword);
//
//        return supervisorRepository.save(supervisor);
//    }


//    public List<Supervisor> getAllSupervisor() {
//        return supervisorRepository.findAll();
//    }

//    @Transactional(readOnly = true)