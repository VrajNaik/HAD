package com.Team12.HADBackEnd.util.CredentialGenerator;

import com.Team12.HADBackEnd.repository.DoctorRepository;
import com.Team12.HADBackEnd.repository.FieldHealthCareWorkerRepository;
import com.Team12.HADBackEnd.repository.ReceptionistRepository;
import com.Team12.HADBackEnd.repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final AtomicInteger supervisorCounter = new AtomicInteger(1);
    private final AtomicInteger fieldHealthcareWorkerCounter = new AtomicInteger(1);
    private final AtomicInteger doctorCounter = new AtomicInteger(1);
    private final AtomicInteger receptionistCounter = new AtomicInteger(1);

    private final SupervisorRepository supervisorRepository;

    private final FieldHealthCareWorkerRepository fieldHealthcareWorkerRepository;

    private final DoctorRepository doctorRepository;

    private final ReceptionistRepository receptionistRepository;

    @Autowired
    public CredentialServiceImpl(SupervisorRepository supervisorRepository,
                                 FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                                 DoctorRepository doctorRepository,
                                 ReceptionistRepository receptionistRepository) {
        this.supervisorRepository = supervisorRepository;
        this.fieldHealthcareWorkerRepository = fieldHealthCareWorkerRepository;
        this.doctorRepository = doctorRepository;
        this.receptionistRepository = receptionistRepository;
    }

    @Override
    public String generateUniqueUsername(String role) {
        String generatedUsername = null;
        boolean isUnique = false;
        int sequenceNumber;

        switch (role.toLowerCase()) {
            case "supervisor":
                while (!isUnique) {
                    sequenceNumber = supervisorCounter.getAndIncrement();
                    generatedUsername = "SV" + String.format("%05d", sequenceNumber);
                    isUnique = !supervisorRepository.existsByUsername(generatedUsername);
                }
                break;
            case "fieldhealthcareworker":
                while (!isUnique) {
                    sequenceNumber = fieldHealthcareWorkerCounter.getAndIncrement();
                    generatedUsername = "FHW" + String.format("%05d", sequenceNumber);
                    isUnique = !fieldHealthcareWorkerRepository.existsByUsername(generatedUsername);
                }
                break;
            case "doctor":
                while (!isUnique) {
                    sequenceNumber = doctorCounter.getAndIncrement();
                    generatedUsername = "DR" + String.format("%05d", sequenceNumber);
                    isUnique = !doctorRepository.existsByUsername(generatedUsername);
                }
                break;
            case "receptionist":
                while (!isUnique) {
                    sequenceNumber = receptionistCounter.getAndIncrement();
                    generatedUsername = "RECP" + String.format("%05d", sequenceNumber);
                    isUnique = !receptionistRepository.existsByUsername(generatedUsername);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid role provided");
        }
        return generatedUsername;
    }

    @Override
    public String generateRandomPassword() {
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