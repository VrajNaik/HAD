package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.*;

import com.Team12.HADBackEnd.payload.request.DistrictDTO;
import com.Team12.HADBackEnd.payload.request.DoctorDTO;
import com.Team12.HADBackEnd.payload.request.DoctorUpdateRequest;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import com.Team12.HADBackEnd.repository.DoctorRepository;
import com.Team12.HADBackEnd.repository.RoleRepository;
import com.Team12.HADBackEnd.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;
    // Autowire the JavaMailSender


    public ResponseEntity<Doctor> updateDoctor(DoctorUpdateRequest request) {
        return doctorRepository.findById(request.getId()).map(doctor -> {
            doctor.setName(request.getName());
            doctor.setLicenseId(request.getLicenseId());
            doctor.setAge(request.getAge());
            doctor.setGender(request.getGender());
            doctor.setSpecialty(request.getSpecialty());
            doctor.setPhoneNum(request.getPhoneNum());
            doctor.setEmail(request.getEmail());
            doctor.setUsername(request.getUsername());
            doctor.setPassword(request.getPassword());
            Doctor savedDoctor = doctorRepository.save(doctor);
            return ResponseEntity.ok().body(savedDoctor);
        }).orElse(ResponseEntity.notFound().build());
    }
    @Transactional(rollbackFor = Exception.class)
    public Doctor addDoctor(Doctor doctor) throws DuplicateLicenseIdException, DuplicateEmailIdException {
        String generatedUsername = generateUniqueUsername();
        String generatedRandomPassword = generateRandomPassword();

        // Print the received Doctor object
        System.out.println("Received Doctor object: " + doctor);

        // Retrieve the associated District object
        District district = doctor.getDistrict();
        System.out.println("Associated District object: " + district);

        // Create new user's account
        User user = new User(generatedUsername,
                doctor.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                .orElseThrow(() -> new RuntimeException("Error: DOCTOR role not found."));
        roles.add(doctorRole);
        user.setRoles(roles);
        userRepository.save(user);

        // Check for duplicate license ID
        if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
            throw new DuplicateLicenseIdException("Doctor with the same license ID already exists.");
        }

        // Check for duplicate email
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DuplicateEmailIdException("Doctor with the same Email ID already exists.");
        }

        // Set doctor's details
        doctor.setUsername(generatedUsername);
        doctor.setPassword(encoder.encode(generatedRandomPassword));
        doctor.setDistrict(district);
//        doctor.setDistrictId(district.getId());

        // Save doctor
        Doctor savedDoctor = doctorRepository.save(doctor);
        System.out.println("Doctor's District: " + savedDoctor.getDistrict());

        // Add the newly saved Doctor to the doctors list of the associated District
//        if (district != null) {
//            district.getDoctors().add(savedDoctor);
//            // Save the updated District
//            districtRepository.save(district);
//        }
//        if (district != null) {
//            District managedDistrict = districtRepository.findById(district.getId())
//                    .orElseThrow(() -> new RuntimeException("District not found."));
//
//            // Print the doctors list of the managed district to check if the newly added doctor is included
//            for (Doctor doctor1 : managedDistrict.getDoctors()) {
//                System.out.println(doctor1.getName());
//            }
//        }

        // Send email with username and password
        System.out.println(generatedRandomPassword);
        sendCredentialsByEmail(savedDoctor.getEmail(), generatedUsername, generatedRandomPassword);

        return savedDoctor;
    }


    //    @Transactional(rollbackFor = Exception.class) // Rollback for any exception
//    public Doctor addDoctor(Doctor doctor) throws DuplicateLicenseIdException, DuplicateEmailIdException {
//        String generatedUsername = generateUniqueUsername();
//        String generatedRandomPassword = generateRandomPassword();
//
//        // Create new user's account
//        User user = new User(generatedUsername,
//                doctor.getEmail(),
//                encoder.encode(generatedRandomPassword));
//
//        Set<Role> roles = new HashSet<>();
//        Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//                .orElseThrow(() -> new RuntimeException("Error: DOCTOR role not found."));
//        roles.add(doctorRole);
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        // Check for duplicate license ID
//        if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
//            throw new DuplicateLicenseIdException("Doctor with the same license ID already exists.");
//        }
//
//        // Check for duplicate email
//        if (doctorRepository.existsByEmail(doctor.getEmail())) {
//            throw new DuplicateEmailIdException("Doctor with the same Email ID already exists.");
//        }
//
//        // Set doctor's details
////        doctor.setUsername(generatedUsername);
////        doctor.setPassword(encoder.encode(generatedRandomPassword));
////
////        // Save doctor
////        doctorRepository.save(doctor);
//        District district = districtRepository.findById(doctor.getDistrictId())
//                .orElseThrow(() -> new RuntimeException("District not found."));
//        System.out.println(district.getName());
//        // Create Doctor object
//        System.out.println(district.getName());
//        doctor.setDistrict(district); // Set the district
//        doctor.setUsername(generatedUsername);
//        doctor.setPassword(encoder.encode(generatedRandomPassword));
//
//
//        // Save doctor
//        doctorRepository.save(doctor);
//
//        // Send email with username and password
//        System.out.println(generatedRandomPassword);
//        sendCredentialsByEmail(doctor.getEmail(), generatedUsername, generatedRandomPassword);
//
//        return doctor;
//    }
//    public List<Doctor> getAllDoctors() {
//        return doctorRepository.findAll();
//    }
//    public List<Doctor> getAll() {
//        return doctorRepository.findAll();
//    }
    public List<DoctorDTO> getAllDoctorsWithDistricts() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();
        // Copy data from Doctor entity to DoctorDTO
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setLicenseId(doctor.getLicenseId());
        doctorDTO.setAge(doctor.getAge());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setGender(doctor.getGender());
        doctorDTO.setSpecialty(doctor.getSpecialty());
        doctorDTO.setUsername(doctor.getUsername());
        doctorDTO.setPassword(doctor.getPassword());
        doctorDTO.setPhoneNum(doctor.getPhoneNum());
        // Set DistrictDTO
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setId(doctor.getDistrict().getId());
        districtDTO.setName(doctor.getDistrict().getName());

        doctorDTO.setDistrict(districtDTO);

        return doctorDTO;
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
    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with username: " + username));

        if (doctor.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Doctor is already " + (active ? "activated" : "deactivated"));
        }

        doctor.setActive(active);
        doctorRepository.save(doctor);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        user.setActive(active);
        userRepository.save(user);
    }
}


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
//        user.setRoles(roles);
//        userRepository.save(user);


//public Doctor addDoctor(Doctor doctor) {
//
//    String generatedUsername = generateUniqueUsername();
//    String generatedRandomPassword = generateRandomPassword();
//
//
//    // Create new user's account
//    User user = new User(generatedUsername,
//            doctor.getEmail(),
//            encoder.encode(generatedRandomPassword));
//
//
//    Set<Role> roles = new HashSet<>();
//    Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
//            .orElseThrow(() -> new RuntimeException("Error: DOCTOR is not found."));
//    roles.add(doctorRole);
//    user.setRoles(roles);
//    userRepository.save(user);
//
//    if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
////            throw new IllegalArgumentException("Doctor with the same license ID already exists.");
//        throw new DuplicateLicenseIdException("Doctor with the same license ID already exists.");
//    }
//    if (doctorRepository.existsByEmail(doctor.getEmail())) {
////            throw new IllegalArgumentException("Doctor with the same license ID already exists.");
//        throw new DuplicateEmailIdException("Doctor with the same Email ID already exists.");
//    }
//    // Generate a unique username
//
//    // String generatedUsername = generateUniqueUsername();
//
//    doctor.setUsername(generatedUsername);
//    doctor.setAge(doctor.getAge());
//    doctor.setDistrict(doctor.getDistrict());
//    doctor.setPhoneNum(doctor.getPhoneNum());
//    doctor.setLicenseId(doctor.getLicenseId());
//    doctor.setSpecialty(doctor.getSpecialty());
//    // Generate a random password
//    doctor.setPassword(generatedRandomPassword);
//    // Send email with username and password
//    userRepository.save(user);
//    sendCredentialsByEmail(doctor.getEmail(), generatedUsername, generatedRandomPassword);
//
//    return doctorRepository.save(doctor);
//}

