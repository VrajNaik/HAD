package com.Team12.HADBackEnd.services.Receptionist;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Receptionist.ReceptionistCreation;
import com.Team12.HADBackEnd.DTOs.Receptionist.ReceptionistDTO;
import com.Team12.HADBackEnd.DTOs.Response.ReceptionistUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.response.ResponseMessage;
import com.Team12.HADBackEnd.repository.*;
import com.Team12.HADBackEnd.services.Doctor.DoctorServiceImpl;
import com.Team12.HADBackEnd.util.CredentialGenerator.CredentialService;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import com.Team12.HADBackEnd.util.MailService.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReceptionistServiceImpl implements ReceptionistService{

    private final ReceptionistRepository receptionistRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorServiceImpl doctorServiceImpl;
    private final DTOConverter dtoConverter;
    private final CitizenRepository citizenRepository;
    private final CredentialService credentialService;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public ReceptionistServiceImpl(ReceptionistRepository receptionistRepository,
                                   DoctorRepository doctorRepository,
                                   DTOConverter dtoConverter,
                                   DoctorServiceImpl doctorServiceImpl,
                                   CitizenRepository citizenRepository,
                                   CredentialService credentialService,
                                   PasswordEncoder passwordEncoder,
                                   RoleRepository roleRepository,
                                   UserRepository userRepository,
                                   EmailService emailService, HospitalRepository hospitalRepository) {
        this.receptionistRepository = receptionistRepository;
        this.doctorRepository = doctorRepository;
        this.dtoConverter = dtoConverter;
        this.doctorServiceImpl = doctorServiceImpl;
        this.citizenRepository = citizenRepository;
        this.credentialService = credentialService;
        this.encoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.hospitalRepository = hospitalRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Receptionist addReceptionist(ReceptionistCreation receptionist) throws DuplicateEmailIdException {
        String generatedUsername = credentialService.generateUniqueUsername("receptionist");
        String generatedRandomPassword = credentialService.generateRandomPassword();

        Hospital hospital = hospitalRepository.findById(receptionist.getHospitalId())
                .orElseThrow(() -> new NotFoundException("Hospital not found"));
        Receptionist receptionist1 = new Receptionist();


        User user = new User(generatedUsername,
                receptionist.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role supervisorRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR role not found."));
        roles.add(supervisorRole);
        user.setRoles(roles);
        userRepository.save(user);

        if (receptionistRepository.existsByEmail(receptionist.getEmail())) {
            throw new DuplicateEmailIdException("Receptionist with the same Email ID already exists.");
        }

        if (receptionistRepository.existsByPhoneNumber(receptionist.getPhoneNumber())) {
            throw new DuplicateEmailIdException("Receptionist with the same Phone Number already exists.");
        }

        receptionist1.setUsername(generatedUsername);
        receptionist1.setPassword(encoder.encode(generatedRandomPassword));
        if(receptionist.getEmail() != null) {
            receptionist1.setEmail(receptionist.getEmail());
        }
        if(receptionist.getPhoneNumber() != null) {
            receptionist1.setPhoneNumber(receptionist.getPhoneNumber());
        }
        receptionist1.setHospital(hospital);
        if (receptionist.getGender() != null) {
            receptionist1.setGender(receptionist.getGender());
        }
        receptionist1.setAge(receptionist.getAge());
        receptionist1.setActive(true);

        Receptionist savedReceptionist = receptionistRepository.save(receptionist1);

        try {
            emailService.sendCredentialsByEmail(savedReceptionist.getEmail(), generatedUsername, generatedRandomPassword);
        }
        catch (MessagingException e) {
            System.out.println("Error in sending Mail !!!");
        }

        return savedReceptionist;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceptionistDTO updateReceptionist(ReceptionistUpdateRequestDTO request) {
        Receptionist receptionist = receptionistRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Receptionist not found with Username: " + request.getUsername()));

        if (receptionistRepository.existsByEmail(request.getEmail()) && !Objects.equals(receptionist.getEmail(), request.getEmail())) {
            throw new DuplicateEmailIdException("Receptionist with the same Email ID already exists.");
        }

        if (receptionistRepository.existsByPhoneNumber(request.getPhoneNumber()) && !Objects.equals(receptionist.getPhoneNumber(), request.getPhoneNumber())) {
            throw new DuplicateEmailIdException("Receptionist with the same Phone Number already exists.");
        }
        if (request.getName() != null) {
            receptionist.setName(request.getName());
        }
        if (request.getAge() != 0) {
            receptionist.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            receptionist.setGender(request.getGender());
        }
        if (request.getEmail() != null) {
            receptionist.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            receptionist.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getHospital().getId() != null) {
            Hospital newHospital = hospitalRepository.findById(request.getHospital().getId())
                    .orElseThrow(() -> new NotFoundException("Hospital not found with id: " + request.getHospital().getId()));
            receptionist.setHospital(newHospital);
        }

        Receptionist updatedReceptionist = receptionistRepository.save(receptionist);
        return dtoConverter.convertToReceptionistDTO(updatedReceptionist);
    }


    @Override
    public ResponseEntity<List<ReceptionistDTO>> getAllRecptionist() {
        List<Receptionist> receptionists = receptionistRepository.findAll();
        List<ReceptionistDTO> receptionistDTOS = receptionists.stream()
                                                 .map(dtoConverter::convertToReceptionistDTO)
                                                 .collect(Collectors.toList());
        return new ResponseEntity<>(receptionistDTOS, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getDoctorsInHospital(String username) {
        Receptionist receptionist = receptionistRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Receptionist Not Found with given Username: " + username));

        Long hospitalId = receptionist.getHospital().getId();
//        List<Doctor> doctors = doctorRepository.findByHospitalId(hospitalId)
//                .orElseThrow(() -> new NotFoundException("No Doctors Found within this Hospital: " + hospitalId));
        List<Doctor> doctors = doctorRepository.findByHospitalIdAndActive(hospitalId, true)
                .orElseThrow(() -> new NotFoundException("No Active Doctors Found within this Hospital: " + hospitalId));

        List<DoctorDTO> doctorDTOS = doctors.stream()
                .map(doctorServiceImpl::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOS);
    }


    @Override
    public ResponseEntity<?> getCitizensAssignedToHospital(String username) {
        Receptionist receptionist = receptionistRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Receptionist Not Found with given Username: " + username));

        Hospital hospital = receptionist.getHospital();

        List<Citizen> citizens = citizenRepository.findByHospital(hospital)
                .orElseThrow(() -> new NotFoundException("No Citizens Found within this Hospital: " + hospital.getName()));
        List<CitizenForDoctorDTO> citizenForDoctorDTOS = citizens.stream()
                .map(dtoConverter::convertToCitizenForDoctorDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citizenForDoctorDTOS);
    }


    @Override
    @Transactional
    public ResponseEntity<?> assignDoctorToCitizen(String abhaId, String doctorUsername) {
        Citizen citizen = citizenRepository.findByAbhaId(abhaId)
                .orElseThrow(() -> new NotFoundException("No Citizen Found with provided ABHA ID:" + abhaId));

        Doctor doctor = doctorRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new NotFoundException("No Doctor Found with provided Username:" + doctorUsername));


        if (!citizen.getDistrict().equals(doctor.getDistrict())) {
            throw new NotFoundException("Doctor and Field Health Care Worker Districts are not same");
        }

        citizen.setDoctor(doctor);
        citizenRepository.save(citizen);
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizen Assigned to the Doctor Successfully!");
    }
}
