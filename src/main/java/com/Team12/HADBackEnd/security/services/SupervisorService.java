package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.repository.DoctorRepository;
import com.Team12.HADBackEnd.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class SupervisorService {

    @Autowired
    private SupervisorRepository supervisorRepository;

    public Supervisor addSupervisor(Supervisor supervisor) {

        // Generate a unique username
        String generatedUsername = generateUniqueUsername();
        supervisor.setUsername(generatedUsername);

        // Generate a random password
        supervisor.setPassword(generateRandomPassword());

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
}