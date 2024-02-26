package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.Doctor;
import com.bezkoder.springjwt.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
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

    public Doctor addDoctor(Doctor doctor) {
        // Check if a doctor with the same license ID already exists
        if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
            throw new IllegalArgumentException("Doctor with the same license ID already exists.");
        }

        // Generate a unique username
        String generatedUsername = generateUniqueUsername();
        doctor.setUsername(generatedUsername);

        // Generate a random password
        doctor.setPassword(generateRandomPassword());

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
}