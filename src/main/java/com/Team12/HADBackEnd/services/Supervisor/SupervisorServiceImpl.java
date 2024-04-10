package com.Team12.HADBackEnd.services.Supervisor;

import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.services.FieldHealthCareWorker.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.repository.*;
import com.Team12.HADBackEnd.util.CredentialGenerator.CredentialService;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import com.Team12.HADBackEnd.util.MailService.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final DistrictRepository districtRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FieldHealthCareWorkerRepository workerRepository;
    private final LocalAreaRepository localAreaRepository;
    private final PasswordEncoder encoder;
    private final FollowUpRepository followUpRepository;
    private final FieldHealthCareWorkerService fieldHealthCareWorkerService;
    private final EmailService emailService;
    private final CredentialService credentialService;
    private final DTOConverter dtoConverter;



    @Autowired
    public SupervisorServiceImpl(
            SupervisorRepository supervisorRepository,
            DistrictRepository districtRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            FieldHealthCareWorkerRepository workerRepository,
            LocalAreaRepository localAreaRepository,
            PasswordEncoder encoder,
            FollowUpRepository followUpRepository,
            FieldHealthCareWorkerService fieldHealthCareWorkerService,
            EmailService emailService,
            CredentialService credentialService,
            DTOConverter dtoConverter
    ) {
        this.supervisorRepository = supervisorRepository;
        this.districtRepository = districtRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
        this.localAreaRepository = localAreaRepository;
        this.encoder = encoder;
        this.followUpRepository = followUpRepository;
        this.fieldHealthCareWorkerService = fieldHealthCareWorkerService;
        this.emailService = emailService;
        this.credentialService = credentialService;
        this.dtoConverter = dtoConverter;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Supervisor addSupervisor(Supervisor supervisor) throws DuplicateEmailIdException {
        String generatedUsername = credentialService.generateUniqueUsername("supervisor");
        String generatedRandomPassword = credentialService.generateRandomPassword();

        District district = supervisor.getDistrict();

        User user = new User(generatedUsername,
                supervisor.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role supervisorRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR role not found."));
        roles.add(supervisorRole);
        user.setRoles(roles);
        userRepository.save(user);

        if (supervisorRepository.existsByEmail(supervisor.getEmail())) {
            throw new DuplicateEmailIdException("Supervisor with the same Email ID already exists.");
        }

        if (supervisorRepository.existsByPhoneNum(supervisor.getPhoneNum())) {
            throw new DuplicateEmailIdException("Supervisor with the same Phone Number already exists.");
        }

        supervisor.setUsername(generatedUsername);
        supervisor.setPassword(encoder.encode(generatedRandomPassword));
        supervisor.setDistrict(district);

        Supervisor savedSupervisor = supervisorRepository.save(supervisor);

        try {
            emailService.sendCredentialsByEmail(savedSupervisor.getEmail(), generatedUsername, generatedRandomPassword);
        }
        catch (MessagingException e) {
            System.out.println("Error in sending Mail !!!");
        }

        return savedSupervisor;
    }

    @Override
    public List<SupervisorForAdminDTO> getAllSupervisorsWithDistricts() {
        List<Supervisor> supervisors = supervisorRepository.findAll();
        return supervisors.stream()
                .map(dtoConverter::convertToSupervisorAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SupervisorDTO updateSupervisor(SupervisorUpdateRequestDTO request) {
        Supervisor supervisor = supervisorRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Supervisor not found with Username: " + request.getUsername()));

        if (supervisorRepository.existsByEmail(request.getEmail()) && !Objects.equals(supervisor.getEmail(), request.getEmail())) {
            throw new DuplicateEmailIdException("Supervisor with the same Email ID already exists.");
        }

        if (supervisorRepository.existsByPhoneNum(request.getPhoneNum()) && !Objects.equals(supervisor.getPhoneNum(), request.getPhoneNum())) {
            throw new DuplicateEmailIdException("Supervisor with the same Phone Number already exists.");
        }
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
        if (request.getPhoneNum() != null) {
            supervisor.setPhoneNum(request.getPhoneNum());
        }
        if (request.getDistrict().getId() != null) {
            District newDistrict = districtRepository.findById(request.getDistrict().getId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getDistrict().getId()));
            supervisor.setDistrict(newDistrict);
        }

        Supervisor updatedSupervisor = supervisorRepository.save(supervisor);
        return convertToDTO(updatedSupervisor);
    }


    @Override
    public SupervisorDTO getSupervisorByUsername(String username) {
        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));
        return convertToDTO(supervisor);
    }

    @Override
    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {
        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));

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

    @Override
    public List<FollowUpsDTO> getFollowUpsForSupervisor(String supervisorUsername) {
        Supervisor supervisor = supervisorRepository.findByUsername(supervisorUsername)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + supervisorUsername));

        Long districtId = supervisor.getDistrict().getId();
        List<FollowUpsDTO> followUpDTOs = new ArrayList<>();

        List<FieldHealthCareWorker> fieldHealthCareWorkers = workerRepository.findByDistrictId(districtId);
        if (!fieldHealthCareWorkers.isEmpty()) {
            for (FieldHealthCareWorker worker : fieldHealthCareWorkers) {
                List<FollowUp> followUps = followUpRepository.findByFieldHealthCareWorkerAndStatus(worker, "Assigned")
                        .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + supervisorUsername));
                if (followUps.isEmpty()) {
                    throw new NotFoundException("No pending Follow Ups for the District Controlled by Supervisor:" + supervisorUsername);
                }
                followUpDTOs.addAll(convertFollowUpsToDTOs(followUps, worker));
            }
        }
        return followUpDTOs;
    }

    private List<FollowUpsDTO> convertFollowUpsToDTOs(List<FollowUp> followUps, FieldHealthCareWorker worker) {
        return followUps.stream()
                .map(followUp -> convertFollowUpToDTO(followUp, worker))
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldHealthcareWorkerDTO> getUnassignedFieldHealthCareWorkerDTOs(String username) {
        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));
        District district = supervisor.getDistrict();
        List<FieldHealthCareWorker> unassignedWorkers = workerRepository.findByLocalAreaIsNullAndDistrictId(district.getId());
        return unassignedWorkers.stream()
                .map(fieldHealthCareWorkerService::convertToDTO2)
                .collect(Collectors.toList());
    }

    @Override
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


    @Override
    public List<LocalAreaDTO> getAllLocalAreasByUsername(String username) {

        Supervisor supervisor = supervisorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));

        District district = supervisor.getDistrict();

        if (district == null) {
            throw new NotFoundException("District not assigned to the supervisor with username: " + username);
        }

        List<LocalArea> localAreas = district.getLocalAreas();

        return localAreas.stream()
                .map(this::convertToLocalAreaDTO)
                .collect(Collectors.toList());
    }

    public LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea) {
        LocalAreaDTO localAreaDTO = new LocalAreaDTO();
        localAreaDTO.setId(localArea.getId());
        localAreaDTO.setName(localArea.getName());
        localAreaDTO.setPincode(localArea.getPincode());
        FieldHealthCareWorker fieldHealthcareWorker = localArea.getFieldHealthCareWorker();
        if(fieldHealthcareWorker != null) {
            FieldHealthcareWorkerDTO fieldHealthcareWorkerDTO = new FieldHealthcareWorkerDTO();
            fieldHealthcareWorkerDTO.setId(fieldHealthcareWorker.getId());
            fieldHealthcareWorkerDTO.setName(fieldHealthcareWorker.getName());
            fieldHealthcareWorkerDTO.setUsername(fieldHealthcareWorker.getUsername());
            fieldHealthcareWorkerDTO.setEmail(fieldHealthcareWorker.getEmail());
            fieldHealthcareWorkerDTO.setPhoneNum(fieldHealthcareWorker.getPhoneNum());
            fieldHealthcareWorkerDTO.setAge(fieldHealthcareWorker.getAge());
            District district = fieldHealthcareWorker.getDistrict();
            if(district != null) {
                DistrictDTO dto = new DistrictDTO();
                dto.setName(district.getName());
                fieldHealthcareWorkerDTO.setDistrict(dto);
            }
            fieldHealthcareWorkerDTO.setGender(fieldHealthcareWorker.getGender());
            localAreaDTO.setFieldHealthcareWorkerDTO(fieldHealthcareWorkerDTO);
        }
        return localAreaDTO;
    }

    public SupervisorDTO convertToDTO(Supervisor supervisor) {
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
        if (supervisor.getPhoneNum() != null) {
            supervisorDTO.setPhoneNum(supervisor.getPhoneNum());
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

    private FollowUpsDTO convertFollowUpToDTO(FollowUp followUp, FieldHealthCareWorker worker) {
        FollowUpsDTO followUpDTO = new FollowUpsDTO();
        followUpDTO.setId(followUp.getId());
        followUpDTO.setDate(followUp.getDate());
        followUpDTO.setStatus(followUp.getStatus());
        followUpDTO.setInstructions(followUp.getInstructions());
        followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
        followUpDTO.setHealthWorkerId(worker.getId());
        followUpDTO.setHealthWorkerName(worker.getName());
        followUpDTO.setHealthWorkerEmail(worker.getEmail());
        followUpDTO.setHealthWorkerPhone(worker.getPhoneNum());
        return followUpDTO;
    }
}


//    public Questionnaire createQuestionnaire(QuestionnaireDTO questionnaireDto) {
//        Questionnaire questionnaire = new Questionnaire();
//        questionnaire.setName(questionnaireDto.getName());
//        questionnaire = questionnaireRepository.save(questionnaire);
//
//        for (QuestionDTO questionDto : questionnaireDto.getQuestions()) {
//            Question question = new Question();
//            question.setQuestionnaire(questionnaire);
//            question.setQuestionText(questionDto.getQuestionText());
//            question = questionRepository.save(question);
//
//            for (String optionText : questionDto.getOptions()) {
//                Option option = new Option();
//                option.setQuestion(question);
//                option.setOptionText(optionText);
//                optionRepository.save(option);
//            }
//        }
//
//        return questionnaire;
//    }



//    private void sendCredentialsByEmail(String email, String username, String password) {
//        // Prepare email message
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(email);
//        mailMessage.setSubject("Credentials for accessing the system");
//        mailMessage.setText("Your username: " + username + "\nYour password: " + password);
//
//        // Send email
//        javaMailSender.send(mailMessage);
//    }


//    public String generateUniqueUsername() {
//        String generatedUsername = null;
//        boolean isUnique = false;
//        while (!isUnique) {
//            // Generate a username starting with "SV" followed by a sequence of numbers
//            int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
//            generatedUsername = "SV" + randomNumber;
//            isUnique = !supervisorRepository.existsByUsername(generatedUsername);
//        }
//        return generatedUsername;
//    }




//@Autowired
//private SupervisorRepository supervisorRepository;
//@Autowired
//private DistrictRepository districtRepository;
//@Autowired
//private RoleRepository roleRepository;
//@Autowired
//private UserRepository userRepository;
//@Autowired
//private FieldHealthCareWorkerRepository workerRepository;
//@Autowired
//private LocalAreaRepository localAreaRepository;
//@Autowired
//private PasswordEncoder encoder;
//@Autowired
//private ICD10CodeRepository icd10CodeRepository;
//@Autowired
//private QuestionnaireRepository questionnaireRepository;
//@Autowired
//private QuestionRepository questionRepository;
//@Autowired
//private FollowUpRepository followUpRepository;
//@Autowired
//private OptionRepository optionRepository;
//@Autowired
//private FieldHealthCareWorkerService fieldHealthCareWorkerService;
//@Autowired
//private JavaMailSender javaMailSender; // Autowire the JavaMailSender
//@Autowired
//private FieldHealthCareWorkerController fieldHealthCareWorkerController;


//@Override
//public List<FollowUpsDTO> getFollowUpsForSupervisor(String supervisorUsername) {
//    Supervisor supervisor = supervisorRepository.findByUsername(supervisorUsername)
//            .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + supervisorUsername));
//
//    Long districtId = supervisor.getDistrict().getId();
//    List<FollowUpsDTO> followUpDTOs = new ArrayList<>();
//
//    List<FieldHealthCareWorker> fieldHealthCareWorkers = workerRepository.findByDistrictId(districtId);
//    if (!fieldHealthCareWorkers.isEmpty()) {
//        for (FieldHealthCareWorker worker : fieldHealthCareWorkers) {
//            List<FollowUp> followUps = followUpRepository.findByFieldHealthCareWorkerAndStatus(worker, "Assigned")
//                    .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + supervisorUsername));
//            if(followUps.isEmpty()) {
//                throw new NotFoundException("No pending Follow Ups for the District Controlled by Supervisor:" + supervisorUsername);
//            }
//            for (FollowUp followUp : followUps) {
//                FollowUpsDTO followUpDTO = new FollowUpsDTO();
//                followUpDTO.setId(followUp.getId());
//                followUpDTO.setDate(followUp.getDate());
//                followUpDTO.setStatus(followUp.getStatus());
//                followUpDTO.setInstructions(followUp.getInstructions());
//                followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
//                followUpDTO.setHealthWorkerId(worker.getId());
//                followUpDTO.setHealthWorkerName(worker.getName());
//                followUpDTO.setHealthWorkerEmail(worker.getEmail());
//                followUpDTO.setHealthWorkerPhone(worker.getPhoneNum());
//                followUpDTOs.add(followUpDTO);
//            }
//        }
//    }
//    return followUpDTOs;
//}
