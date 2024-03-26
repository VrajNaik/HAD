package com.Team12.HADBackEnd.security.services;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.request.*;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FieldHealthCareWorkerService {

    @Autowired
    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    @Autowired
    private LocalAreaRepository localAreaRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JavaMailSender javaMailSender; // Autowire the JavaMailSender


    @Transactional(rollbackFor = Exception.class)
    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker worker) throws DuplicateEmailIdException {
        String generatedUsername = generateUniqueUsername();
        String generatedRandomPassword = generateRandomPassword();

        // Retrieve the associated District object
        District district = worker.getDistrict();

        // Create new user's account
        User user = new User(generatedUsername,
                worker.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role supervisorRole = roleRepository.findByName(ERole.ROLE_FIELD_HEALTHCARE_WORKER)
                .orElseThrow(() -> new RuntimeException("Error: FIELD HEALTHCARE WORKER role not found."));
        roles.add(supervisorRole);
        user.setRoles(roles);
        userRepository.save(user);

        // Check for duplicate email
        if (fieldHealthCareWorkerRepository.existsByEmail(worker.getEmail())) {
            throw new DuplicateEmailIdException("Field Healthcare Worker with the same Email ID already exists.");
        }

        // Set supervisor's details
        worker.setUsername(generatedUsername);
        worker.setPassword(encoder.encode(generatedRandomPassword));
        worker.setDistrict(district);

        // Save supervisor
        FieldHealthCareWorker savedWorker = fieldHealthCareWorkerRepository.save(worker);

        // Send email with username and password
        sendCredentialsByEmail(savedWorker.getEmail(), generatedUsername, generatedRandomPassword);

        return savedWorker;
    }

    public List<FieldHealthcareWorkerDTO> getAllFieldHealthCareWorkersWithDistricts() {
        List<FieldHealthCareWorker> workers = fieldHealthCareWorkerRepository.findAll();
        return workers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public FieldHealthcareWorkerDTO updateFieldHealthCareWorker(SupervisorUpdateRequestDTO request) {
        FieldHealthCareWorker worker  = fieldHealthCareWorkerRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Field HealthCare Worker not found with id: " + request.getId()));

        if (request.getName() != null) {
            worker.setName(request.getName());
        }
        if (request.getAge() != 0) {
            worker.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            worker.setGender(request.getGender());
        }
        if (request.getEmail() != null) {
            worker.setEmail(request.getEmail());
        }
        if (request.getNewDistrictId() != null) {
            District newDistrict = districtRepository.findById(request.getNewDistrictId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getNewDistrictId()));
            worker.setDistrict(newDistrict);
        }

        FieldHealthCareWorker updatedWorker = fieldHealthCareWorkerRepository.save(worker);
        return convertToDTO(updatedWorker);
    }
    private FieldHealthcareWorkerDTO convertToDTO(FieldHealthCareWorker worker) {
        FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
        workerDTO.setId(worker.getId());

        if (worker.getName() != null) {
            workerDTO.setName(worker.getName());
        }
        if (worker.getAge() != 0) {
            workerDTO.setAge(worker.getAge());
        }
        if (worker.getGender() != null) {
            workerDTO.setGender(worker.getGender());
        }
        if (worker.getEmail() != null) {
            workerDTO.setEmail(worker.getEmail());
        }
        if (worker.getUsername() != null) {
            workerDTO.setUsername(worker.getUsername());
        }
        if (worker.getPassword() != null) {
            workerDTO.setPassword(worker.getPassword());
        }
        if (worker.getDistrict() != null) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setId(worker.getDistrict().getId());
            districtDTO.setName(worker.getDistrict().getName());
            workerDTO.setDistrict(districtDTO);
        }
        if (worker.getLocalArea() != null) {
            LocalAreaDTO localAreaDTO = new LocalAreaDTO();
            localAreaDTO.setId(worker.getLocalArea().getId());
            localAreaDTO.setName(worker.getLocalArea().getName());
            localAreaDTO.setPincode(worker.getLocalArea().getPincode());
            workerDTO.setLocalArea(localAreaDTO);
        }
        return workerDTO;
    }

//    @Transactional
//    public void setActiveStatusByUsername(String username, boolean active) {
//        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
//                .orElseThrow(() -> new DoctorNotFoundException("Field Health Care Worker not found with username: " + username));
//
//        if (worker.isActive() == active) {
//            throw new DoctorAlreadyDeactivatedException("Field Health Care Worker is already " + (active ? "activated" : "deactivated"));
//        }
//
//        worker.setActive(active);
//        fieldHealthCareWorkerRepository.save(worker);
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
//        user.setActive(active);
//        userRepository.save(user);
//    }

    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {
        // Find the field health care worker by username
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException("Field Health Care Worker not found with username: " + username));

        // Check if the field health care worker's active status is already the desired status
        if (worker.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Field Health Care Worker is already " + (active ? "activated" : "deactivated"));
        }

        // Check if any associated fields are not null
        if (worker.getDistrict() != null || worker.getLocalArea() != null || !worker.getCitizens().isEmpty() || !worker.getHealthRecords().isEmpty() || !worker.getFollowUps().isEmpty()) {
            StringBuilder message = new StringBuilder("Cannot deactivate field health care worker due to associated entities:\n");

            if (worker.getDistrict() != null) {
                message.append(" - This worker is associated with a district.\n");
            }

            if (worker.getLocalArea() != null) {
                message.append(" - This worker is associated with a local area.\n");
            }

            if (!worker.getCitizens().isEmpty()) {
                message.append(" - This worker is associated with citizens.\n");
            }

            if (!worker.getHealthRecords().isEmpty()) {
                message.append(" - This worker is associated with health records.\n");
            }

            if (!worker.getFollowUps().isEmpty()) {
                message.append(" - This worker is associated with follow-up records.\n");
            }

            throw new DoctorNotFoundException(message.toString());
        }

        // Update the active status of the field health care worker
        worker.setActive(active);
        fieldHealthCareWorkerRepository.save(worker);

        // Find the corresponding user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Update the active status of the user
        user.setActive(active);
        userRepository.save(user); // Save changes
    }


    public FieldHealthcareWorkerDTO getFieldHealthcareWorkerByUsername(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException("Field Health Care Worker not found with username: " + username));
        if (worker == null) {
            // Handle the case where no worker is found with the provided username
            System.out.println("Hello\n");
            return null;
        }
        return convertToDTO2(worker);
    }


    private FieldHealthcareWorkerDTO convertToDTO2(FieldHealthCareWorker worker) {
        FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
        workerDTO.setId(worker.getId());

        if (worker.getName() != null) {
            workerDTO.setName(worker.getName());
        }
        if (worker.getAge() != 0) {
            workerDTO.setAge(worker.getAge());
        }
        if (worker.getGender() != null) {
            workerDTO.setGender(worker.getGender());
        }
        if (worker.getEmail() != null) {
            workerDTO.setEmail(worker.getEmail());
        }
        if (worker.getUsername() != null) {
            workerDTO.setUsername(worker.getUsername());
        }
        if (worker.getPassword() != null) {
            workerDTO.setPassword(worker.getPassword());
        }
        if (worker.getDistrict() != null) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setId(worker.getDistrict().getId());
            districtDTO.setName(worker.getDistrict().getName());
            workerDTO.setDistrict(districtDTO);
        }
        if (worker.getLocalArea() != null) {
            LocalAreaDTO localAreaDTO = new LocalAreaDTO();
            localAreaDTO.setId(worker.getLocalArea().getId());
            localAreaDTO.setName(worker.getLocalArea().getName());
            localAreaDTO.setPincode(worker.getLocalArea().getPincode());
            workerDTO.setLocalArea(localAreaDTO);
        }
        return workerDTO;
    }

    public CitizenDTO registerCitizen(CitizenRegistrationDTO citizenDTO) {
        // Fetch the field healthcare worker by ID
        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(citizenDTO.getFieldHealthCareWorkerId())
                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + citizenDTO.getFieldHealthCareWorkerId()));

        // Fetch the doctor by ID
        Doctor doctor = doctorRepository.findById(citizenDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + citizenDTO.getDoctorId()));

        // Map DTO to Citizen entity
        Citizen citizen = new Citizen();
        citizen.setName(citizenDTO.getName());
        citizen.setAge(citizenDTO.getAge());
        citizen.setGender(citizenDTO.getGender());
        citizen.setAddress(citizenDTO.getAddress());
        citizen.setConsent(citizenDTO.isConsent());
        citizen.setStatus(citizenDTO.isStatus());
        citizen.setState(citizenDTO.getState());
        citizen.setAbhaId(citizenDTO.getAbhaId());
        citizen.setFieldHealthCareWorker(fieldHealthCareWorker); // Assign the fetched worker to the citizen
        citizen.setDoctor(doctor); // Assign the fetched doctor to the citizen

        // Fetch the pincode and district from the associated LocalArea and set to the citizen
        LocalArea localArea = fieldHealthCareWorker.getLocalArea();
        String pincode = localArea.getPincode();
        citizen.setPincode(pincode);
        String district = localArea.getDistrict().getName();
        citizen.setDistrict(district);

        // Save the citizen entity
        Citizen savedCitizen = citizenRepository.save(citizen);

        // Map saved citizen and worker to DTO
        CitizenDTO citizenResponse = mapToCitizenDTO(savedCitizen);

        return citizenResponse;
    }


    private CitizenDTO mapToCitizenDTO(Citizen citizen) {
        CitizenDTO citizenDTO = new CitizenDTO();
        citizenDTO.setId(citizen.getId());
        citizenDTO.setName(citizen.getName());
        citizenDTO.setAge(citizen.getAge());
        citizenDTO.setGender(citizen.getGender());
        citizenDTO.setAddress(citizen.getAddress());
        citizenDTO.setConsent(citizen.isConsent());
        citizenDTO.setPincode(citizen.getPincode());
        citizenDTO.setStatus(citizen.isStatus());
        citizenDTO.setState(citizen.getState());
        citizenDTO.setDistrict(citizen.getDistrict());
        citizenDTO.setAbhaId(citizen.getAbhaId());

        // Map FieldHealthCareWorker to FieldHealthCareWorkerDTO
        FieldHealthCareWorker fieldHealthCareWorker = citizen.getFieldHealthCareWorker();
        if (fieldHealthCareWorker != null) {
            FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
            workerDTO.setId(fieldHealthCareWorker.getId());
            workerDTO.setName(fieldHealthCareWorker.getName());
            workerDTO.setAge(fieldHealthCareWorker.getAge());
            workerDTO.setGender(fieldHealthCareWorker.getGender());

            workerDTO.setUsername(fieldHealthCareWorker.getUsername());

            workerDTO.setPassword(fieldHealthCareWorker.getPassword());
            workerDTO.setEmail(fieldHealthCareWorker.getEmail());
            citizenDTO.setFieldHealthCareWorker(workerDTO);
            if (fieldHealthCareWorker.getDistrict() != null) {
                DistrictDTO districtDTO = new DistrictDTO();
                districtDTO.setId(fieldHealthCareWorker.getDistrict().getId());
                districtDTO.setName(fieldHealthCareWorker.getDistrict().getName());
                workerDTO.setDistrict(districtDTO);
            }
            if (fieldHealthCareWorker.getLocalArea() != null) {
                LocalAreaDTO localAreaDTO = new LocalAreaDTO();
                localAreaDTO.setId(fieldHealthCareWorker.getLocalArea().getId());
                localAreaDTO.setName(fieldHealthCareWorker.getLocalArea().getName());
                localAreaDTO.setPincode(fieldHealthCareWorker.getLocalArea().getPincode());
                workerDTO.setLocalArea(localAreaDTO);
            }
        }

        // Map Doctor to DoctorDTO
        Doctor doctor = citizen.getDoctor();
        if (doctor != null) {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setAge(doctor.getAge());
            doctorDTO.setGender(doctor.getGender());
            doctorDTO.setPhoneNum(doctor.getPhoneNum());
            doctorDTO.setUsername(doctor.getUsername());
            doctorDTO.setSpecialty(doctor.getSpecialty());
            doctorDTO.setPassword(doctor.getPassword());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setLicenseId(doctor.getLicenseId());
            if (doctor.getDistrict() != null) {
                DistrictDTO districtDTO = new DistrictDTO();
                districtDTO.setId(doctor.getDistrict().getId());
                districtDTO.setName(doctor.getDistrict().getName());
                doctorDTO.setDistrict(districtDTO);
            }
            citizenDTO.setDoctorDTO(doctorDTO);
        }

        return citizenDTO;
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

//    public FieldHealthCareWorker addFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {
//
//        // Generate a unique username
//        String generatedUsername = generateUniqueUsername();
//        fieldHealthCareWorker.setUsername(generatedUsername);
//
//        // Generate a random password
//        String generatedPassword = generateRandomPassword();
//        fieldHealthCareWorker.setPassword(generatedPassword);
//
//        // Save the user
//        FieldHealthCareWorker savedUser = fieldHealthCareWorkerRepository.save(fieldHealthCareWorker);
//
//        // Send email with username and password
//        sendCredentialsByEmail(savedUser.getEmail(), generatedUsername, generatedPassword);
//
//        return savedUser;
//    }

//    public List<FieldHealthCareWorker> getAllFieldHealthCareWorker() {
//        return fieldHealthCareWorkerRepository.findAll();
//    }

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

//    public DistrictDTO convertToDTO(District district) {
//        System.out.println("Hello2\n");
//        DistrictDTO districtDTO = new DistrictDTO();
//        districtDTO.setId(district.getId());
//        districtDTO.setName(district.getName());
//        return districtDTO;
//    }
//    public LocalAreaDTO convertToDTO(LocalArea localArea) {
//        System.out.println("Hello3\n");
//        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
//        localAreaDTO.setId(localArea.getId());
//        localAreaDTO.setName(localArea.getName());
//        return localAreaDTO;
//    }


//    public Citizen registerCitizen(Citizen citizen) {
//        return citizenRepository.save(citizen);
//    }

//    public Citizen registerCitizen(CitizenRegistrationDTO citizenDTO) {
//        // Fetch the field healthcare worker by ID
//        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(citizenDTO.getFieldHealthCareWorkerId())
//                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + citizenDTO.getFieldHealthCareWorkerId()));
//
//        // Map DTO to Citizen entity
//        Citizen citizen = new Citizen();
//        citizen.setName(citizenDTO.getName());
//        citizen.setAge(citizenDTO.getAge());
//        citizen.setGender(citizenDTO.getGender());
//        citizen.setAddress(citizenDTO.getAddress());
//        citizen.setConsent(citizenDTO.isConsent());
//        citizen.setPincode(citizenDTO.getPincode());
//        citizen.setStatus(citizenDTO.isStatus());
//        citizen.setState(citizenDTO.getState());
//        citizen.setDistrict(citizenDTO.getDistrict());
//        citizen.setAbhaId(citizenDTO.getAbhaId());
//        citizen.setFieldHealthCareWorker(fieldHealthCareWorker); // Assign the fetched worker to the citizen
//
//        // Save the citizen entity
//        return citizenRepository.save(citizen);
//    }


//    public CitizenDTO registerCitizen(CitizenRegistrationDTO citizenDTO) {
//        // Fetch the field healthcare worker by ID
//        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(citizenDTO.getFieldHealthCareWorkerId())
//                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + citizenDTO.getFieldHealthCareWorkerId()));
//
//        // Map DTO to Citizen entity
//        Citizen citizen = new Citizen();
//        citizen.setName(citizenDTO.getName());
//        citizen.setAge(citizenDTO.getAge());
//        citizen.setGender(citizenDTO.getGender());
//        citizen.setAddress(citizenDTO.getAddress());
//        citizen.setConsent(citizenDTO.isConsent());
//        citizen.setPincode(citizenDTO.getPincode());
//        citizen.setStatus(citizenDTO.isStatus());
//        citizen.setState(citizenDTO.getState());
//        citizen.setDistrict(citizenDTO.getDistrict());
//        citizen.setAbhaId(citizenDTO.getAbhaId());
//        citizen.setFieldHealthCareWorker(fieldHealthCareWorker); // Assign the fetched worker to the citizen
//
//        // Save the citizen entity
//        Citizen savedCitizen = citizenRepository.save(citizen);
//
//        // Map saved citizen and worker to DTO
//        CitizenDTO citizenResponse = mapToCitizenDTO(savedCitizen);
//
//        return citizenResponse;
//    }
//
//    private CitizenDTO mapToCitizenDTO(Citizen citizen) {
//        CitizenDTO citizenDTO = new CitizenDTO();
//        citizenDTO.setId(citizen.getId());
//        citizenDTO.setName(citizen.getName());
//        citizenDTO.setAge(citizen.getAge());
//        citizenDTO.setGender(citizen.getGender());
//        citizenDTO.setAddress(citizen.getAddress());
//        citizenDTO.setConsent(citizen.isConsent());
//        citizenDTO.setPincode(citizen.getPincode());
//        citizenDTO.setStatus(citizen.isStatus());
//        citizenDTO.setState(citizen.getState());
//        citizenDTO.setDistrict(citizen.getDistrict());
//        citizenDTO.setAbhaId(citizen.getAbhaId());
//
//        // Map FieldHealthCareWorker to FieldHealthCareWorkerDTO
//        FieldHealthCareWorker fieldHealthCareWorker = citizen.getFieldHealthCareWorker();
//        if (fieldHealthCareWorker != null) {
//            FieldHealthcareWorkerDTO workerDTO = new FieldHealthcareWorkerDTO();
//            workerDTO.setId(fieldHealthCareWorker.getId());
//            workerDTO.setName(fieldHealthCareWorker.getName());
//            // Map other fields as needed
//            citizenDTO.setFieldHealthCareWorker(workerDTO);
//        }
//
//        return citizenDTO;
//    }



//    public CitizenDTO registerCitizen(CitizenRegistrationDTO citizenDTO) {
//        // Fetch the field healthcare worker by ID
//        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(citizenDTO.getFieldHealthCareWorkerId())
//                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + citizenDTO.getFieldHealthCareWorkerId()));
//
//        // Fetch the doctor by ID
//        Doctor doctor = doctorRepository.findById(citizenDTO.getDoctorId())
//                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + citizenDTO.getDoctorId()));
//
//        // Map DTO to Citizen entity
//        Citizen citizen = new Citizen();
//        citizen.setName(citizenDTO.getName());
//        citizen.setAge(citizenDTO.getAge());
//        citizen.setGender(citizenDTO.getGender());
//        citizen.setAddress(citizenDTO.getAddress());
//        citizen.setConsent(citizenDTO.isConsent());
//        citizen.setPincode(citizenDTO.getPincode());
//        citizen.setStatus(citizenDTO.isStatus());
//        citizen.setState(citizenDTO.getState());
//        citizen.setDistrict(citizenDTO.getDistrict());
//        citizen.setAbhaId(citizenDTO.getAbhaId());
//        citizen.setFieldHealthCareWorker(fieldHealthCareWorker); // Assign the fetched worker to the citizen
//        citizen.setDoctor(doctor); // Assign the fetched doctor to the citizen
//
//        // Save the citizen entity
//        Citizen savedCitizen = citizenRepository.save(citizen);
//
//        // Map saved citizen and worker to DTO
//        CitizenDTO citizenResponse = mapToCitizenDTO(savedCitizen);
//
//        return citizenResponse;
//    }