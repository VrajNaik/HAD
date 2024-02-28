package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.Doctor;

import com.Team12.HADBackEnd.repository.DoctorRepository;
import com.Team12.HADBackEnd.repository.RoleRepository;
import com.Team12.HADBackEnd.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


//
//@Service
//public class DoctorService {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    public Doctor addDoctor(Doctor doctor) {
//        // Generate username based on the first letter of the name and last name
//        String[] nameParts = doctor.getName().split(" ");
//        String username = nameParts[0].substring(0, 1).toLowerCase() + nameParts[nameParts.length - 1].toLowerCase();
//        doctor.setUsername(username);
//
//        // Generate a random password
//        doctor.setPassword(generateRandomPassword());
//
//        return doctorRepository.save(doctor);
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
//}
//
//@Service
//public class DoctorService {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    public Doctor addDoctor(Doctor doctor) {
//        // Generate username based on "DR" followed by a sequence of numbers
//        String generatedUsername = generateUsername();
//        doctor.setUsername(generatedUsername);
//
//        // Generate a random password
//        doctor.setPassword(generateRandomPassword());
//
//        return doctorRepository.save(doctor);
//    }
//
//    private String generateUsername() {
//        // You may want to fetch the last used doctor ID from the database and increment it
//        // For simplicity, I'm just generating a random number here
//        int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
//        return "DR" + randomNumber;
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
//}

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;
    // Autowire the JavaMailSender

    public Doctor addDoctor(Doctor doctor) {

        // Check if a doctor with the same license ID already exists

        String generatedUsername = generateUniqueUsername();
        String generatedRandomPassword = generateRandomPassword();

        //paste here

        if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
            throw new IllegalArgumentException("Doctor with the same license ID already exists.");
        }

        // Generate a unique username
        // String generatedUsername = generateUniqueUsername();
        doctor.setUsername(generatedUsername);
        doctor.setAge(doctor.getAge());
        doctor.setDistrict(doctor.getDistrict());
        doctor.setPhoneNum(doctor.getPhoneNum());
        doctor.setLicenseId(doctor.getLicenseId());
        doctor.setSpecialty(doctor.getSpecialty());
        // Generate a random password
        doctor.setPassword(generatedRandomPassword);
        // Send email with username and password
        sendCredentialsByEmail(doctor.getEmail(), generatedUsername, generatedRandomPassword);

        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    private String generateUniqueUsername() {
        String generatedUsername = null;
        boolean isUnique = false;
        while (!isUnique) {
            // Generate a username starting with "DR" followed by a sequence of numbers
            int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
            generatedUsername = "DR" + randomNumber;
            isUnique = !doctorRepository.existsByUsername(generatedUsername);
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



//        // Create new user's account
//        User user = new User(generatedUsername,
//                doctor.getEmail(),
//                encoder.encode(generatedRandomPassword));
//
//        Set<String> strRoles = new HashSet<>();
//        strRoles.add("user");
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//                    .orElseThrow(() -> new RuntimeException("Error: DOCTOR is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                    case "supervisor":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
//                                .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR is not found."));
//                        roles.add(modRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);