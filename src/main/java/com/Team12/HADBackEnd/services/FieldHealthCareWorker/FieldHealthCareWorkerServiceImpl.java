package com.Team12.HADBackEnd.services.FieldHealthCareWorker;

import com.Team12.HADBackEnd.DTOs.Citizen.*;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.AssignDoctorRequest;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.UpdateFollowUpStatusListRequest;
import com.Team12.HADBackEnd.DTOs.FollowUp.UpdateFollowUpStatusRequest;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.Hospital.HospitalDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.DTOs.Response.ResponseDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.*;
import com.Team12.HADBackEnd.DTOs.District.DistrictDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.payload.response.FollowUpReturnDTO;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class FieldHealthCareWorkerServiceImpl implements FieldHealthCareWorkerService {

    private final FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    private final DistrictRepository districtRepository;
    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final FollowUpRepository followUpRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final ResponseRepository responseRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final CredentialService credentialService;
    private final DTOConverter dtoConverter;
    private final DoctorServiceImpl doctorServiceImpl;

    @Autowired
    public FieldHealthCareWorkerServiceImpl(FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                                        DistrictRepository districtRepository,
                                        DoctorRepository doctorRepository,
                                        RoleRepository roleRepository,
                                        UserRepository userRepository,
                                        CitizenRepository citizenRepository,
                                        FollowUpRepository followUpRepository,
                                        HealthRecordRepository healthRecordRepository,
                                        ResponseRepository responseRepository,
                                        HospitalRepository hospitalRepository,
                                        PasswordEncoder passwordEncoder,
                                        EmailService emailService,
                                        CredentialService credentialService,
                                        DTOConverter dtoConverter,
                                        DoctorServiceImpl doctorServiceImpl) {
        this.fieldHealthCareWorkerRepository = fieldHealthCareWorkerRepository;
        this.districtRepository = districtRepository;
        this.doctorRepository = doctorRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.citizenRepository = citizenRepository;
        this.followUpRepository = followUpRepository;
        this.healthRecordRepository = healthRecordRepository;
        this.responseRepository = responseRepository;
        this.hospitalRepository = hospitalRepository;
        this.encoder = passwordEncoder;
        this.emailService = emailService;
        this.credentialService = credentialService;
        this.dtoConverter = dtoConverter;
        this.doctorServiceImpl = doctorServiceImpl;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker worker) throws DuplicateEmailIdException {
        String generatedUsername = credentialService.generateUniqueUsername("FieldHealthCareWorker");
        String generatedRandomPassword = credentialService.generateRandomPassword();

        District district = worker.getDistrict();

        User user = new User(generatedUsername,
                worker.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role supervisorRole = roleRepository.findByName(ERole.ROLE_FIELD_HEALTHCARE_WORKER)
                .orElseThrow(() -> new RuntimeException("Error: FIELD HEALTHCARE WORKER role not found."));
        roles.add(supervisorRole);
        user.setRoles(roles);
        userRepository.save(user);

        if (fieldHealthCareWorkerRepository.existsByEmail(worker.getEmail())) {
            throw new DuplicateEmailIdException("Field Healthcare Worker with the same Email ID already exists.");
        }
        if (fieldHealthCareWorkerRepository.existsByPhoneNum(worker.getPhoneNum())) {
            throw new DuplicateEmailIdException("Field Healthcare Worker with the same Phone Number already exists.");
        }

        worker.setUsername(generatedUsername);
        worker.setPassword(encoder.encode(generatedRandomPassword));
        worker.setDistrict(district);

        FieldHealthCareWorker savedWorker = fieldHealthCareWorkerRepository.save(worker);

        try {
            emailService.sendCredentialsByEmail(savedWorker.getEmail(), generatedUsername, generatedRandomPassword);
        }
        catch (MessagingException e) {
            System.out.println("Error in Sending Mail !!!");
        }

        return savedWorker;
    }


    @Override
    public List<FieldHealthCareWorkerForAdminDTO> getAllFieldHealthCareWorkersWithDistricts() {
        List<FieldHealthCareWorker> workers = fieldHealthCareWorkerRepository.findAll();
        return workers.stream()
                .map(dtoConverter::convertToFieldHealthCareWorkerForAdminDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FieldHealthCareWorkerWithHealthRecordDTO updateFieldHealthCareWorker(FieldHealthCareWorkerUpdateRequestDTO request) {
        FieldHealthCareWorker worker  = fieldHealthCareWorkerRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Field HealthCare Worker not found with Username: " + request.getUsername()));

        if (fieldHealthCareWorkerRepository.existsByEmail(request.getEmail()) && !Objects.equals(worker.getEmail(), request.getEmail())) {
            throw new DuplicateEmailIdException("Field Healthcare Worker with the same Email ID already exists.");
        }
        if (fieldHealthCareWorkerRepository.existsByPhoneNum(request.getPhoneNum()) && !Objects.equals(worker.getPhoneNum(), request.getPhoneNum())) {
            throw new DuplicateEmailIdException("Field Healthcare Worker with the same Phone Number already exists.");
        }
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
        if (request.getPhoneNum() != null) {
            worker.setPhoneNum(request.getPhoneNum());
        }
        if (request.getDistrict().getId() != null) {
            District newDistrict = districtRepository.findById(request.getDistrict().getId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getDistrict().getId()));
            worker.setDistrict(newDistrict);
        }

        FieldHealthCareWorker updatedWorker = fieldHealthCareWorkerRepository.save(worker);
        return dtoConverter.convertToFieldHealthCareWorkerWithHealthRecordDTO(updatedWorker);
    }


    @Override
    public ResponseEntity<?> registerCitizen(CitizenRegistrationDTO citizenDTO) {

        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findByUsername(citizenDTO.getFieldHealthCareWorkerUsername())
                .orElseThrow(() -> new NotFoundException("Field healthcare worker not found with username: " + citizenDTO.getFieldHealthCareWorkerUsername()));

//        Doctor doctor = doctorRepository.findByUsername(citizenDTO.getDoctorUsername())
//                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + citizenDTO.getDoctorUsername()));

        Citizen citizen = new Citizen();
        if(citizenDTO.getName() != null && !citizenDTO.getName().isEmpty()) {
            citizen.setName(citizenDTO.getName());
        }
        if(citizenDTO.getAbhaId() != null) {
            citizen.setAbhaId(citizenDTO.getAbhaId());
        }

        citizen.setAge(citizenDTO.getAge());
        if(citizenDTO.getGender() != null && !citizenDTO.getGender().isEmpty()) {
            citizen.setGender(citizenDTO.getGender());
        }
        if (citizenDTO.getPincode() != null && !citizenDTO.getPincode().isEmpty()) {
            citizen.setPincode(citizenDTO.getPincode());
        }

        if (citizenDTO.getAddress() != null && !citizenDTO.getAddress().isEmpty()) {
            citizen.setAddress(citizenDTO.getAddress());
        }
        citizen.setConsent(citizenDTO.isConsent());
        citizen.setStatus(citizenDTO.getStatus());
        if (citizenDTO.getState() != null && !citizenDTO.getState().isEmpty()) {
            citizen.setState(citizenDTO.getState());
        }
        if (citizenDTO.getAbhaId() != null && !citizenDTO.getAbhaId().isEmpty()) {
            citizen.setAbhaId(citizenDTO.getAbhaId());
        }
        citizen.setFieldHealthCareWorker(fieldHealthCareWorker);
//        citizen.setDoctor(doctor);

        LocalArea localArea = fieldHealthCareWorker.getLocalArea();
        String pincode = localArea.getPincode();
        citizen.setPincode(pincode);
        District district = localArea.getDistrict();
        citizen.setDistrict(district);

        citizenRepository.save(citizen);
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizen Registration Done successfully!");
    }

    @Override
    public ResponseEntity<?> registerCitizens(CitizensRegistrationDTO citizensDTO) {
        List<CitizenRegistrationDTO> citizenDTOList = citizensDTO.getCitizens();
        for (CitizenRegistrationDTO citizenDTO : citizenDTOList) {
            FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findByUsername(citizenDTO.getFieldHealthCareWorkerUsername())
                    .orElseThrow(() -> new NotFoundException("Field healthcare worker not found with username: " + citizenDTO.getFieldHealthCareWorkerUsername()));

//            Doctor doctor = doctorRepository.findByUsername(citizenDTO.getDoctorUsername())
//                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + citizenDTO.getDoctorUsername()));

            Citizen citizen = new Citizen();
            if(citizenDTO.getName() != null && !citizenDTO.getName().isEmpty()) {
                citizen.setName(citizenDTO.getName());
            }
            citizen.setAge(citizenDTO.getAge());
            if(citizenDTO.getGender() != null && !citizenDTO.getGender().isEmpty()) {
                citizen.setGender(citizenDTO.getGender());
            }
            if (citizenDTO.getPincode() != null && !citizenDTO.getPincode().isEmpty()) {
                citizen.setPincode(citizenDTO.getPincode());
            }

            if (citizenDTO.getAddress() != null && !citizenDTO.getAddress().isEmpty()) {
                citizen.setAddress(citizenDTO.getAddress());
            }
            citizen.setConsent(true);

            citizen.setStatus("new");
            if (citizenDTO.getState() != null && !citizenDTO.getState().isEmpty()) {
                citizen.setState(citizenDTO.getState());
            }
            if (citizenDTO.getAbhaId() != null && !citizenDTO.getAbhaId().isEmpty()) {
                citizen.setAbhaId(citizenDTO.getAbhaId());
            }
            citizen.setFieldHealthCareWorker(fieldHealthCareWorker);
//            citizen.setDoctor(doctor);

            LocalArea localArea = fieldHealthCareWorker.getLocalArea();
            String pincode = localArea.getPincode();
            citizen.setPincode(pincode);
            District district = localArea.getDistrict();
            citizen.setDistrict(district);


            citizenRepository.save(citizen);
        }

        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizens Registration Done successfully!");
    }

    @Override
    public ResponseEntity<?> getDoctorsByFHWUsername(String username) {
        FieldHealthCareWorker fhw = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));

        Long districtId = fhw.getDistrict().getId();

        List<Doctor> doctorsInDistrict = doctorRepository.findAllByDistrictId(districtId);

        List<DoctorDTO> doctorDTOs = doctorsInDistrict.stream()
                .map(doctorServiceImpl::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOs);
    }

    @Override
    public ResponseEntity<?> addResponse(ResponseDTO responseDTO) {
        Citizen citizen = citizenRepository.findByAbhaId(responseDTO.getAbhaId())
                .orElseThrow(() -> new NotFoundException("No Citizen Found with provided ABHA ID:" + responseDTO.getAbhaId()));

//        Long maxFollowUpNo = responseRepository.findTopByCitizenOrderByFollowUpNoDesc(citizen)
//                .orElse(0L);
//
//        Long newFollowUpNo = maxFollowUpNo + 1;

        Response response = new Response();
        response.setScore(responseDTO.getScore());
        response.setCitizen(citizen);
//        response.setFollowUpNo(newFollowUpNo);
        response.setAnswers(responseDTO.getAnswers());

        responseRepository.save(response);
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizen Response added successfully!");
    }


    @Override
    public ResponseEntity<?> addResponses(List<ResponseDTO> responseDTOs) {
        for (ResponseDTO responseDTO : responseDTOs) {
            Citizen citizen = citizenRepository.findByAbhaId(responseDTO.getAbhaId())
                    .orElseThrow(() -> new NotFoundException("No Citizen Found with provided ABHA ID:" + responseDTO.getAbhaId()));

            Response response = new Response();
            response.setScore(responseDTO.getScore());
            response.setCitizen(citizen);
            response.setAnswers(responseDTO.getAnswers());

            responseRepository.save(response);
        }

        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizen Responses added successfully!");
    }


    @Override
    @Transactional
    public ResponseEntity<?> assignDoctorToCitizen(String abhaId, String doctorUsername) {
        Citizen citizen = citizenRepository.findByAbhaId(abhaId)
                .orElseThrow(() -> new NotFoundException("No Citizen Found with provided ABHA ID:" + abhaId));

        Doctor doctor = doctorRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new NotFoundException("No Doctor Found with provided Username:" + doctorUsername));


        // Check if citizen and doctor belong to the same district
        if (!citizen.getDistrict().equals(doctor.getDistrict())) {
            throw new NotFoundException("Doctor and Field Health Care Worker Districts are not same");
        }

        // Assign the doctor to the citizen
        citizen.setDoctor(doctor);
        citizenRepository.save(citizen);
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizen Assigned to the Doctor Successfully!");
    }


    @Override
    @Transactional
    public ResponseEntity<?> assignDoctorsToCitizens(List<AssignDoctorRequest> doctorAssignments) {
        for (AssignDoctorRequest assignment : doctorAssignments) {
            String abhaId = assignment.getAbhaId();
            String doctorUsername = assignment.getDoctorUsername();

            Citizen citizen = citizenRepository.findByAbhaId(abhaId)
                    .orElseThrow(() -> new NotFoundException("No Citizen Found with provided ABHA ID:" + abhaId));

            Doctor doctor = doctorRepository.findByUsername(doctorUsername)
                    .orElseThrow(() -> new NotFoundException("No Doctor Found with provided Username:" + doctorUsername));



            // Check if citizen and doctor belong to the same district
            if (!citizen.getDistrict().equals(doctor.getDistrict())) {
                throw new NotFoundException("Doctor and Field Health Care Worker Districts are not same");
            }

            // Assign the doctor to the citizen
            citizen.setDoctor(doctor);
            citizenRepository.save(citizen);
        }
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Citizens Assigned to the Doctors Successfully!");
    }


    @Override
    public ResponseEntity<?> getHospitalsInDistrict(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("No Field Health Care Worker found with the given username: " + username));
        Long districtId = worker.getDistrict().getId();
        List<Hospital> hospitals = hospitalRepository.findByDistrictId(districtId)
                .orElseThrow(() -> new NotFoundException("No Hospital Found in the given District:" + districtId));

        List<HospitalDTO> hospitalDTOS = hospitals.stream()
                .map(dtoConverter::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hospitalDTOS);
    }


    @Override
    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {

        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));
        if (worker.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Field Health Care Worker is already " + (active ? "activated" : "deactivated"));
        }
        if (worker.getLocalArea() != null || !worker.getCitizens().isEmpty() || !worker.getHealthRecords().isEmpty() || !worker.getFollowUps().isEmpty()) {
            StringBuilder message = getStringBuilder(worker);

            throw new NotFoundException(message.toString());
        }

        worker.setActive(active);
        fieldHealthCareWorkerRepository.save(worker);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setActive(active);
        userRepository.save(user);
    }

    private static StringBuilder getStringBuilder(FieldHealthCareWorker worker) {
        StringBuilder message = new StringBuilder("Cannot deactivate field health care worker due to associated entities:\n");


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
        return message;
    }


    @Override
    public FieldHealthCareWorkerWithHealthRecordDTO getFieldHealthcareWorkerByUsername(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));
        return dtoConverter.convertToFieldHealthCareWorkerWithHealthRecordDTO(worker);
    }


    @Override
    public List<FieldHealthCareWorkerWithHealthRecordDTO> getFieldHealthCareWorkerDTOs(Long districtId) {
        List<FieldHealthCareWorker> unassignedWorkers = fieldHealthCareWorkerRepository.findByDistrictId(districtId);
        return unassignedWorkers.stream()
                .map(dtoConverter::convertToFieldHealthCareWorkerWithHealthRecordDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<CitizenForFHWDTO> getCitizensByFHWId(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Healthcare Worker not found with this username:" + username));
        List<Citizen> citizens = citizenRepository.findByFieldHealthCareWorker(worker)
                .orElseThrow(() -> new NotFoundException("Citizens not found with worker: " + username));
        List<FollowUp> allFollowUps = followUpRepository.findByFieldHealthCareWorker(worker)
                .orElseThrow(() -> new NotFoundException("FollowUps not found with worker: " + username));
        if(allFollowUps.isEmpty()) {
            throw new NotFoundException("FollowUps not found with worker: " + username);
        }
        List<FollowUpReturnDTO> followUpsForToday = getFollowUpsForToday(username);
        List<CitizenForFHWDTO> citizenDTOs = new ArrayList<>();
        for (Citizen citizen : citizens) {
            // Filter follow-ups for this citizen
            List<FollowUpReturnDTO> citizenFollowUps = followUpsForToday.stream()
                    .filter(followUp -> followUp.getId().equals(citizen.getId()))
                    .collect(Collectors.toList());
            CitizenForFHWDTO dto = dtoConverter.convertToCitizenForFHWDTO(citizen, citizenFollowUps);
            citizenDTOs.add(dto);
        }
        return citizenDTOs;
    }


    @Override
    public List<FollowUpReturnDTO> getFollowUpsForToday(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Healthcare Worker not found with this username:" + username));
        Date today = new Date();
        List<FollowUp> allFollowUps = followUpRepository.findByFieldHealthCareWorker(worker)
                .orElseThrow(() -> new NotFoundException("FollowUps not found with worker: " + username));
        if(allFollowUps.isEmpty()) {
            throw new NotFoundException("FollowUps not found with worker: " + username);
        }
        return filterFollowUpsForToday(allFollowUps, today)
                .stream()
                .map(dtoConverter::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<FollowUpReturnDTO> getFollowUpsForTodayNew(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Healthcare Worker not found with this username:" + username));
        Date today = new Date();
        List<FollowUp> allFollowUps = followUpRepository.findByFieldHealthCareWorker(worker)
                .orElseThrow(() -> new NotFoundException("FollowUps not found with worker: " + username));
        if(allFollowUps.isEmpty()) {
            throw new NotFoundException("FollowUps not found with worker: " + username);
        }
        return filterFollowUpsForToday(allFollowUps, today)
                .stream()
                .map(dtoConverter::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<FollowUp> filterFollowUpsForToday(List<FollowUp> followUps, Date today) {
        return followUps.stream()
                .filter(followUp -> followUp.getDate().equals(today) || isRecurringFollowUpForToday(followUp, today))
                .collect(Collectors.toList());
    }


    private boolean isRecurringFollowUpForToday(FollowUp followUp, Date today) {
        Date recurrenceStartTime = followUp.getRecurrenceStartTime();
        Date recurrenceEndTime = followUp.getRecurrenceEndTime();

        return recurrenceStartTime != null && recurrenceEndTime != null &&
                today.after(recurrenceStartTime) && today.before(recurrenceEndTime) &&
                isTodayMatchingFrequency(followUp, today);
    }


    private boolean isTodayMatchingFrequency(FollowUp followUp, Date today) {
        Frequency frequency = followUp.getFrequency();
        Date followUpDate = followUp.getDate(); // Keep as Date

        // Convert today to LocalDate to perform comparisons
        LocalDate localToday = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localFollowUpDate = followUpDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return switch (frequency) {
            case DAILY -> true;
            case WEEKLY -> localToday.getDayOfWeek() == localFollowUpDate.getDayOfWeek();
            case TWICE_A_WEEK -> localToday.getDayOfWeek() == localFollowUpDate.getDayOfWeek() ||
                    localToday.getDayOfWeek() == localFollowUpDate.plusDays(3).getDayOfWeek();
            case ALTERNATE_DAY -> ChronoUnit.DAYS.between(localToday, localFollowUpDate) % 2 != 0;
            case MONTHLY -> localToday.getDayOfMonth() == localFollowUpDate.getDayOfMonth();
            case TWICE_A_MONTH -> localToday.getDayOfMonth() == localFollowUpDate.getDayOfMonth() ||
                    localToday.getDayOfMonth() == localFollowUpDate.plusDays(15).getDayOfMonth();
            case ALTERNATE_MONTH -> ChronoUnit.MONTHS.between(localToday, localFollowUpDate) % 2 != 0;
            case QUARTERLY -> (localToday.getMonthValue() % 3) == (localFollowUpDate.getMonthValue() % 3);
            case BIANNUALLY -> (localToday.getMonthValue() % 6) == (localFollowUpDate.getMonthValue() % 6);
            case ANNUALLY -> localToday.getMonthValue() == localFollowUpDate.getMonthValue() &&
                    localToday.getDayOfMonth() == localFollowUpDate.getDayOfMonth();
            default -> false;
        };
    }


    @Override
    public int calculateScore(List<String> answers) {
        if (answers.size() != 10) {
            throw new IllegalArgumentException("Exactly 10 answers are required.");
        }

        int score = 0;
        for (String answer : answers) {
            switch (answer.toUpperCase()) {
                case "A":
                    score += 3;
                    break;
                case "B":
                    score += 2;
                    break;
                case "C":
                    score += 1;
                    break;
                case "D":
                    break;
                default:
                    throw new IllegalArgumentException("Invalid answer: " + answer);
            }
        }
        return score;
    }


    @Override
    public void updateFollowUpStatus(UpdateFollowUpStatusRequest request) {
        FollowUp followUp = followUpRepository.findById(request.getFollowUpId())
                .orElseThrow(() -> new NotFoundException("Follow-up not found with ID: " + request.getFollowUpId()));

        followUp.setStatus(request.getStatus());
        followUp.setMeasureOfVitals(request.getInstructions());
        followUpRepository.save(followUp);
    }


    @Override
    public void updateFollowUpStatusList(UpdateFollowUpStatusListRequest request) {
        List<UpdateFollowUpStatusRequest> followUpStatusRequests = request.getFollowUpInstructionsRequests();

        for (UpdateFollowUpStatusRequest followUpStatusRequest : followUpStatusRequests) {
            Long followUpId = followUpStatusRequest.getFollowUpId();
            FollowUp followUp = followUpRepository.findById(followUpId)
                    .orElseThrow(() -> new NotFoundException("Follow-up not found with ID: " + followUpId));

            if (followUpStatusRequest.getStatus() != null) {
                followUp.setStatus(followUpStatusRequest.getStatus());
            }
            if (followUpStatusRequest.getInstructions() != null) {
                followUp.setMeasureOfVitals(followUpStatusRequest.getInstructions());
            }

            followUpRepository.save(followUp);
        }
    }


    @Override
    public ResponseEntity<?> getHealthRecordByCitizenId(Long citizenId) {
        HealthRecord healthRecord = healthRecordRepository.findByCitizen_Id(citizenId)
                .orElseThrow(() -> new NotFoundException("Citizen not found with ID: " + citizenId));

        return ResponseEntity.ok(dtoConverter.convertToHealthRecordDTO(healthRecord));
    }


    @Override
    public ResponseEntity<?> getFollowUpsByHealthRecordId(Long healthRecordId) {
        List<FollowUp> followUps = followUpRepository.findByHealthRecordId(healthRecordId)
                .orElseThrow(() -> new NotFoundException("FollowUps not found with ID: " + healthRecordId));

        if (followUps == null || followUps.isEmpty()) {
            throw new NotFoundException("FollowUps not found with ID: " + healthRecordId);
        }

        return ResponseEntity.ok(followUps.stream()
                .map(dtoConverter::convertToFollowUpDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public List<CitizenForAdminDTO> getAllCitizens() {
        List<Citizen> citizens = citizenRepository.findByStatus("ongoing");
        List<CitizenForAdminDTO> citizenDTOs = new ArrayList<>();
        for (Citizen citizen : citizens) {
            citizenDTOs.add(dtoConverter.convertToCitizenForAdminDTO(citizen));
        }
        return citizenDTOs;
    }


    public boolean isLastFollowUp(Long followUpId) {
        FollowUp followUp = followUpRepository.findById(followUpId).orElse(null);
        if (followUp != null) {
            if (followUp.getFrequency() != Frequency.NONE && followUp.getRecurrenceStartTime() != null && followUp.getRecurrenceEndTime() != null) {
                Date nextOccurrence = calculateNextOccurrence(followUp);
                return nextOccurrence == null;
            } else {
                return true;
            }
        }
        return false;
    }


    private Date calculateNextOccurrence(FollowUp followUp) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        Date recurrenceStartTime = followUp.getRecurrenceStartTime();
        Date recurrenceEndTime = followUp.getRecurrenceEndTime();
        Frequency frequency = followUp.getFrequency();

        if (recurrenceStartTime.after(today) || recurrenceEndTime.before(today))
            return null;

        return switch (frequency) {
            case DAILY -> getNextDailyOccurrence(calendar, recurrenceEndTime);
            case WEEKLY -> getNextWeeklyOccurrence(calendar, recurrenceEndTime);
            case TWICE_A_WEEK -> getNextTwiceAWeekOccurrence(calendar, recurrenceEndTime);
            case ALTERNATE_DAY -> getNextAlternateDayOccurrence(calendar, recurrenceEndTime);
            case EVERY_FEW_DAYS -> getNextEveryFewDaysOccurrence(calendar, recurrenceEndTime);
            case MONTHLY -> getNextMonthlyOccurrence(calendar, recurrenceEndTime);
            case TWICE_A_MONTH -> getNextTwiceAMonthOccurrence(calendar, recurrenceEndTime);
            case ALTERNATE_MONTH -> getNextAlternateMonthOccurrence(calendar, recurrenceEndTime);
            case EVERY_FEW_WEEKS -> getNextEveryFewWeeksOccurrence(calendar, recurrenceEndTime);
            case QUARTERLY -> getNextQuarterlyOccurrence(calendar, recurrenceEndTime);
            case BIANNUALLY -> getNextBiannuallyOccurrence(calendar, recurrenceEndTime);
            case ANNUALLY -> getNextAnnuallyOccurrence(calendar, recurrenceEndTime);
            default -> null;
        };
    }


    private Date getNextDailyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }


    private Date getNextWeeklyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return calendar.getTime();
    }


    private Date getNextTwiceAWeekOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, 3);
        }
        return calendar.getTime();
    }


    private Date getNextAlternateDayOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
        }
        return calendar.getTime();
    }


    private Date getNextEveryFewDaysOccurrence(Calendar calendar, Date recurrenceEndTime) {
        int days = 2;
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, days);
        }
        return calendar.getTime();
    }


    private Date getNextMonthlyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.MONTH, 1);
        }
        return calendar.getTime();
    }


    private Date getNextTwiceAMonthOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, 15);
        }
        return calendar.getTime();
    }


    private Date getNextAlternateMonthOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.MONTH, 2);
        }
        return calendar.getTime();
    }


    private Date getNextEveryFewWeeksOccurrence(Calendar calendar, Date recurrenceEndTime) {
        int weeks = 2;
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        return calendar.getTime();
    }


    private Date getNextQuarterlyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.MONTH, 3);
        }
        return calendar.getTime();
    }


    private Date getNextBiannuallyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.MONTH, 6);
        }
        return calendar.getTime();
    }


    private Date getNextAnnuallyOccurrence(Calendar calendar, Date recurrenceEndTime) {
        while (calendar.getTime().before(recurrenceEndTime)) {
            calendar.add(Calendar.YEAR, 1);
        }
        return calendar.getTime();
    }


    @Override
    public Optional<FollowUp> findAssignedFollowUpByAbhaId(String abhaId) {
        Optional<Citizen> citizenOptional = citizenRepository.findByAbhaId(abhaId);
        if (citizenOptional.isPresent()) {
            HealthRecord healthRecord = citizenOptional.get().getHealthRecord();
            if (healthRecord != null) {
                List<FollowUp> followUps = healthRecord.getFollowUps();
                for (FollowUp followUp : followUps) {
                    if ("assigned".equals(followUp.getStatus())) {
                        return Optional.of(followUp);
                    }
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public FieldHealthCareWorkerWithHealthRecordDTO convertToFieldHealthCareWorkerWithHealthRecordDTO(FieldHealthCareWorker worker) {
        FieldHealthCareWorkerWithHealthRecordDTO workerDTO = new FieldHealthCareWorkerWithHealthRecordDTO();
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
        if (worker.getPhoneNum() != null) {
            workerDTO.setPhoneNum(worker.getPhoneNum());
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
        if(worker.getHealthRecords() != null) {
            List<HealthRecordDTO> healthRecordDTOs = worker.getHealthRecords().stream()
                    .map(dtoConverter::convertToHealthRecordDTO)
                    .collect(Collectors.toList());
            workerDTO.setHealthRecord(healthRecordDTOs);
        }
        return workerDTO;
    }
}
