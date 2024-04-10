package com.Team12.HADBackEnd.services.FieldHealthCareWorker;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizenRegistrationDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICDCodesDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.*;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.FollowUpReturnDTO;
import com.Team12.HADBackEnd.repository.*;
import com.Team12.HADBackEnd.util.CredentialGenerator.CredentialService;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import com.Team12.HADBackEnd.util.MailService.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FieldHealthCareWorkerService {

    @Autowired
    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
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
    private FollowUpRepository followUpRepository;
    @Autowired
    private HealthRecordRepository healthRecordRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private DTOConverter dtoConverter;


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

    public List<FieldHealthCareWorkerForAdminDTO> getAllFieldHealthCareWorkersWithDistricts() {
        List<FieldHealthCareWorker> workers = fieldHealthCareWorkerRepository.findAll();
        return workers.stream()
                .map(dtoConverter::convertToFieldHealthCareWorkerForAdminDTO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public FieldHealthcareWorkerDTO updateFieldHealthCareWorker(FieldHealthCareWorkerUpdateRequestDTO request) {
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
        if (worker.getPhoneNum() != null) {
            workerDTO.setPhoneNum(worker.getPhoneNum());
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

    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {

        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));
        if (worker.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Field Health Care Worker is already " + (active ? "activated" : "deactivated"));
        }
        if (worker.getLocalArea() != null || !worker.getCitizens().isEmpty() || !worker.getHealthRecords().isEmpty() || !worker.getFollowUps().isEmpty()) {
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

            throw new NotFoundException(message.toString());
        }

        worker.setActive(active);
        fieldHealthCareWorkerRepository.save(worker);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setActive(active);
        userRepository.save(user);
    }

    public FieldHealthcareWorkerDTO getFieldHealthcareWorkerByUsername(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Supervisor not found with username: " + username));
        return convertToDTO2(worker);
    }


    public List<FieldHealthcareWorkerDTO> getFieldHealthCareWorkerDTOs(Long districtId) {
        List<FieldHealthCareWorker> unassignedWorkers = fieldHealthCareWorkerRepository.findByDistrictId(districtId);
        return unassignedWorkers.stream()
                .map(this::convertToDTO2)
                .collect(Collectors.toList());
    }


    public FieldHealthcareWorkerDTO convertToDTO2(FieldHealthCareWorker worker) {
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
                    .map(this::convertToHealthRecordDTO)
                    .collect(Collectors.toList());
            workerDTO.setHealthRecord(healthRecordDTOs);
        }
        return workerDTO;
    }

    private HealthRecordDTO convertToHealthRecordDTO(HealthRecord healthRecord) {
        HealthRecordDTO healthRecordDTO = new HealthRecordDTO();
        healthRecordDTO.setId(healthRecord.getId());
        healthRecordDTO.setPrescriptions(healthRecord.getPrescriptions());
        healthRecordDTO.setConclusion(healthRecord.getConclusion());
        healthRecordDTO.setDiagnosis(healthRecord.getDiagnosis());
        healthRecordDTO.setTimestamp(healthRecord.getTimestamp());
        healthRecordDTO.setStatus(healthRecord.getStatus());
        if(healthRecord.getCitizen() != null) {
            Citizen citizen = healthRecord.getCitizen();
            CitizenDTO citizenDTO = new CitizenDTO();
            citizenDTO.setName(citizen.getName());
            citizenDTO.setAge(citizen.getAge());
            citizenDTO.setId(citizen.getId());
            citizenDTO.setAbhaId(citizen.getAbhaId());
            citizenDTO.setAddress(citizen.getAbhaId());
            citizenDTO.setConsent(citizen.isConsent());
            citizenDTO.setDistrict(citizen.getDistrict().getName());
            citizenDTO.setGender(citizen.getGender());
            citizenDTO.setPincode(citizen.getPincode());
            healthRecordDTO.setCitizenDTO(citizenDTO);
        }
        if(healthRecord.getDoctor() != null) {
            Doctor doctor = healthRecord.getDoctor();
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setName(doctor.getName());
            doctorDTO.setAge(doctor.getAge());
            doctorDTO.setId(doctor.getId());
            doctorDTO.setGender(doctor.getGender());
            doctorDTO.setLicenseId(doctor.getLicenseId());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setUsername(doctor.getUsername());
            doctorDTO.setSpecialty(doctor.getSpecialty());
            doctorDTO.setPhoneNum(doctor.getPhoneNum());
            if(doctor.getDistrict() != null) {
                District district = doctor.getDistrict();
                DistrictDTO districtDTO = new DistrictDTO();
                districtDTO.setId(district.getId());
                districtDTO.setName(district.getName());
                doctorDTO.setDistrict(districtDTO);
            }
            healthRecordDTO.setDoctorDTO(doctorDTO);
        }

        if (healthRecord.getFollowUps() != null && !healthRecord.getFollowUps().isEmpty()) {
            List<FollowUpDTO> followUpDTOs = healthRecord.getFollowUps().stream()
                    .map(this::convertToFollowUpDTO)
                    .collect(Collectors.toList());
            healthRecordDTO.setFollowUps(followUpDTOs);
        }

        if (healthRecord.getIcd10Codes() != null && !healthRecord.getIcd10Codes().isEmpty()) {
            List<ICDCodesDTO> icd10CodesDTOs = healthRecord.getIcd10Codes().stream()
                    .map(this::convertToICDCodesDTO)
                    .collect(Collectors.toList());
            healthRecordDTO.setIcd10codes(icd10CodesDTOs);
        }

        return healthRecordDTO;
    }

    private FollowUpDTO convertToFollowUpDTO(FollowUp followUp) {
        FollowUpDTO followUpDTO = new FollowUpDTO();
        followUpDTO.setId(followUp.getId());
        followUpDTO.setDate(followUp.getDate());
        followUpDTO.setStatus(followUp.getStatus());
        followUpDTO.setInstructions(followUp.getInstructions());
        followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
        return followUpDTO;
    }

    private ICDCodesDTO convertToICDCodesDTO(ICD10Code icd10Code) {
        ICDCodesDTO icdCodesDTO = new ICDCodesDTO();
        icdCodesDTO.setId(icd10Code.getId());
        icdCodesDTO.setCode(icd10Code.getCode());
        icdCodesDTO.setName(icd10Code.getName());
        icdCodesDTO.setDescription(icd10Code.getDescription());
        return icdCodesDTO;
    }

    public CitizenDTO registerCitizen(CitizenRegistrationDTO citizenDTO) {

        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(citizenDTO.getFieldHealthCareWorkerId())
                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + citizenDTO.getFieldHealthCareWorkerId()));

        Doctor doctor = doctorRepository.findById(citizenDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + citizenDTO.getDoctorId()));

        Citizen citizen = new Citizen();
        citizen.setName(citizenDTO.getName());
        citizen.setAge(citizenDTO.getAge());
        citizen.setGender(citizenDTO.getGender());
        citizen.setAddress(citizenDTO.getAddress());
        citizen.setConsent(citizenDTO.isConsent());
        citizen.setStatus(citizenDTO.getStatus());
        citizen.setState(citizenDTO.getState());
        citizen.setAbhaId(citizenDTO.getAbhaId());
        citizen.setFieldHealthCareWorker(fieldHealthCareWorker);
        citizen.setDoctor(doctor);

        LocalArea localArea = fieldHealthCareWorker.getLocalArea();
        String pincode = localArea.getPincode();
        citizen.setPincode(pincode);
        District district = localArea.getDistrict();
        citizen.setDistrict(district);

        Citizen savedCitizen = citizenRepository.save(citizen);

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
        citizenDTO.setStatus(citizen.getStatus());
        citizenDTO.setState(citizen.getState());
        citizenDTO.setDistrict(citizen.getDistrict().getName());
        citizenDTO.setAbhaId(citizen.getAbhaId());

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
            workerDTO.setPhoneNum(fieldHealthCareWorker.getPhoneNum());
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

    public List<FollowUpReturnDTO> getFollowUpsForToday(String username) {
        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Healthcare Worker not found with this username:" + username));
        Date today = new Date();
        List<FollowUp> allFollowUps = followUpRepository.findByFieldHealthCareWorker(worker)
                .orElseThrow(() -> new HealthRecordNotFoundException("FollowUps not found with worker: " + username));
        if(allFollowUps.isEmpty()) {
            throw new HealthRecordNotFoundException("FollowUps not found with worker: " + username);
        }
        return filterFollowUpsForToday(allFollowUps, today)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    private FollowUpReturnDTO mapToDTO(FollowUp followUp) {
        return new FollowUpReturnDTO(
                followUp.getId(),
                followUp.getDate(),
                followUp.getStatus(),
                followUp.getInstructions()
        );
    }

    private List<FollowUp> filterFollowUpsForToday(List<FollowUp> followUps, Date today) {
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
        switch (frequency) {
            case DAILY:
                return true;

            case WEEKLY:

                return today.getDay() == followUp.getDate().getDay();

            case TWICE_A_WEEK:

                return today.getDay() == followUp.getDate().getDay() ||
                        today.getDay() == (followUp.getDate().getDay() + 3) % 7;

            case ALTERNATE_DAY:

                return (today.getTime() - followUp.getDate().getTime()) / (1000 * 60 * 60 * 24) % 2 != 0;

            case MONTHLY:

                return today.getDate() == followUp.getDate().getDate();

            case TWICE_A_MONTH:

                return today.getDate() == followUp.getDate().getDate() ||
                        today.getDate() == followUp.getDate().getDate() + 15;

            case ALTERNATE_MONTH:

                return (today.getMonth() - followUp.getDate().getMonth()) % 2 != 0;

            case QUARTERLY:

                return (today.getMonth() % 3) == (followUp.getDate().getMonth() % 3);

            case BIANNUALLY:

                return (today.getMonth() % 6) == (followUp.getDate().getMonth() % 6);

            case ANNUALLY:

                return today.getMonth() == followUp.getDate().getMonth() &&
                        today.getDate() == followUp.getDate().getDate();
            default:
                return false;
        }
    }

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

    public ResponseEntity<?> getDoctorsByFHWUsername(String username) {
        FieldHealthCareWorker fhw = fieldHealthCareWorkerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Field Health Care Worker not found with username: " + username));

        Long districtId = fhw.getDistrict().getId();

        List<Doctor> doctorsInDistrict = doctorRepository.findAllByDistrictId(districtId);

        List<DoctorDTO> doctorDTOs = doctorsInDistrict.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorDTOs);
    }
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();

        doctorDTO.setId(doctor.getId());

        if (doctor.getName() != null) {
            doctorDTO.setName(doctor.getName());
        }
        if (doctor.getLicenseId() != null) {
            doctorDTO.setLicenseId(doctor.getLicenseId());
        }
        if (doctor.getAge() != 0) {
            doctorDTO.setAge(doctor.getAge());
        }
        if (doctor.getEmail() != null) {
            doctorDTO.setEmail(doctor.getEmail());
        }
        if (doctor.getGender() != null) {
            doctorDTO.setGender(doctor.getGender());
        }
        if (doctor.getSpecialty() != null) {
            doctorDTO.setSpecialty(doctor.getSpecialty());
        }
        if (doctor.getUsername() != null) {
            doctorDTO.setUsername(doctor.getUsername());
        }
        if (doctor.getPassword() != null) {
            doctorDTO.setPassword(doctor.getPassword());
        }
        if (doctor.getPhoneNum() != null) {
            doctorDTO.setPhoneNum(doctor.getPhoneNum());
        }

        if (doctor.getDistrict() != null) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setId(doctor.getDistrict().getId());
            districtDTO.setName(doctor.getDistrict().getName());
            doctorDTO.setDistrict(districtDTO);
        }
        return doctorDTO;
    }
    public void updateFollowUpStatus(Long followUpId, String status) {
        FollowUp followUp = followUpRepository.findById(followUpId)
                .orElseThrow(() -> new FollowUpNotFoundException("Follow-up not found with ID: " + followUpId));

        followUp.setStatus(status);
        followUpRepository.save(followUp);
    }

    public ResponseEntity<?> getHealthRecordByCitizenId(Long citizenId) {
        HealthRecord healthRecord = healthRecordRepository.findByCitizen_Id(citizenId)
                .orElseThrow(() -> new HealthRecordNotFoundException("Citizen not found with ID: " + citizenId));

        return ResponseEntity.ok(convertToHealthRecordDTO(healthRecord));
    }

    public ResponseEntity<?> getFollowUpsByHealthRecordId(Long healthRecordId) {
        List<FollowUp> followUps = followUpRepository.findByHealthRecordId(healthRecordId)
                .orElseThrow(() -> new HealthRecordNotFoundException("FollowUps not found with ID: " + healthRecordId));

        if (followUps == null || followUps.isEmpty()) {
            throw new HealthRecordNotFoundException("FollowUps not found with ID: " + healthRecordId);
        }

        return ResponseEntity.ok(followUps.stream()
                .map(this::convertToFollowUpDTO)
                .collect(Collectors.toList()));
    }

    public List<CitizenForAdminDTO> getAllCitizens() {
        List<Citizen> citizens = citizenRepository.findByStatus("ongoing");
        List<CitizenForAdminDTO> citizenDTOs = new ArrayList<>();
        for (Citizen citizen : citizens) {
            citizenDTOs.add(toDTO(citizen));
        }
        return citizenDTOs;
    }

    public static CitizenForAdminDTO toDTO(Citizen citizen) {
        CitizenForAdminDTO dto = new CitizenForAdminDTO();
        dto.setId(citizen.getId());
        dto.setName(citizen.getName());
        dto.setAge(citizen.getAge());
        dto.setGender(citizen.getGender());
        dto.setAddress(citizen.getAddress());
        dto.setConsent(citizen.isConsent());
        dto.setPincode(citizen.getPincode());
        dto.setStatus(citizen.getStatus());
        dto.setState(citizen.getState());
        if(citizen.getDistrict() != null) {
            DistrictDTO district = new DistrictDTO();
            district.setName(citizen.getDistrict().getName());
            district.setId(citizen.getDistrict().getId());
            dto.setDistrict(district);
        }
        dto.setAbhaId(citizen.getAbhaId());
        return dto;
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

        switch (frequency) {
            case DAILY:
                return getNextDailyOccurrence(calendar, recurrenceEndTime);
            case WEEKLY:
                return getNextWeeklyOccurrence(calendar, recurrenceEndTime);
            case TWICE_A_WEEK:
                return getNextTwiceAWeekOccurrence(calendar, recurrenceEndTime);
            case ALTERNATE_DAY:
                return getNextAlternateDayOccurrence(calendar, recurrenceEndTime);
            case EVERY_FEW_DAYS:
                return getNextEveryFewDaysOccurrence(calendar, recurrenceEndTime);
            case MONTHLY:
                return getNextMonthlyOccurrence(calendar, recurrenceEndTime);
            case TWICE_A_MONTH:
                return getNextTwiceAMonthOccurrence(calendar, recurrenceEndTime);
            case ALTERNATE_MONTH:
                return getNextAlternateMonthOccurrence(calendar, recurrenceEndTime);
            case EVERY_FEW_WEEKS:
                return getNextEveryFewWeeksOccurrence(calendar, recurrenceEndTime);
            case QUARTERLY:
                return getNextQuarterlyOccurrence(calendar, recurrenceEndTime);
            case BIANNUALLY:
                return getNextBiannuallyOccurrence(calendar, recurrenceEndTime);
            case ANNUALLY:
                return getNextAnnuallyOccurrence(calendar, recurrenceEndTime);
            default:
                return null;
        }
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


//    public List<FieldHealthcareWorkerDTO> getUnassignedFieldHealthCareWorkerDTOs() {
//        List<FieldHealthCareWorker> unassignedWorkers = fieldHealthCareWorkerRepository.findByLocalAreaIsNull();
//        return unassignedWorkers.stream()
//                .map(this::convertToDTO2)
//                .collect(Collectors.toList());
//    }


//public List<FollowUpReturnDTO> getFollowUpsForToday(String username) {
//        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
//                .orElseThrow(() -> new RoleNotFoundException("Healthcare Worker not found with this username:" + username));
//        Date today = new Date();
//        List<FollowUp> allFollowUps = followUpRepository.findByFieldHealthCareWorker(worker)
//                .orElseThrow(() -> new HealthRecordNotFoundException("FollowUps not found with worker: " + username));
//        if(allFollowUps.isEmpty()) {
//            throw new HealthRecordNotFoundException("FollowUps not found with worker: " + username);
//        }
//        return filterFollowUpsForToday(allFollowUps, today)
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//    private FollowUpReturnDTO mapToDTO(FollowUp followUp) {
//        return new FollowUpReturnDTO(
//                followUp.getId(),
//                followUp.getDate(),
//                followUp.getStatus(),
//                followUp.getInstructions()
//        );
//    }
//
//    private List<FollowUp> filterFollowUpsForToday(List<FollowUp> followUps, Date today) {
//        // Filter follow-ups based on whether they are recurring or not recurring
//        return followUps.stream()
//                .filter(followUp -> followUp.getDate().equals(today) || isRecurringFollowUpForToday(followUp, today))
//                .collect(Collectors.toList());
//    }
//
//    private boolean isRecurringFollowUpForToday(FollowUp followUp, Date today) {
//        Date recurrenceStartTime = followUp.getRecurrenceStartTime();
//        Date recurrenceEndTime = followUp.getRecurrenceEndTime();
//
//        // Check if the follow-up falls within the recurrence time frame
//        return recurrenceStartTime != null && recurrenceEndTime != null &&
//                today.after(recurrenceStartTime) && today.before(recurrenceEndTime) &&
//                isTodayMatchingFrequency(followUp, today);
//    }
//    private boolean isTodayMatchingFrequency(FollowUp followUp, Date today) {
//        Frequency frequency = followUp.getFrequency();
//        switch (frequency) {
//            case DAILY:
//                return true;
//            case WEEKLY:
//                // Check if today is the same day of the week as the follow-up date
//                return today.getDay() == followUp.getDate().getDay();
//            case TWICE_A_WEEK:
//                // Check if today is one of the two days specified for follow-up
//                return today.getDay() == followUp.getDate().getDay() ||
//                        today.getDay() == (followUp.getDate().getDay() + 3) % 7; // 3 days after the initial day
//            case ALTERNATE_DAY:
//                // Check if the day difference between today and follow-up date is odd
//                return (today.getTime() - followUp.getDate().getTime()) / (1000 * 60 * 60 * 24) % 2 != 0;
//            case MONTHLY:
//                // Check if today's day of the month matches the follow-up date's day of the month
//                return today.getDate() == followUp.getDate().getDate();
//            case TWICE_A_MONTH:
//                // Check if today is one of the two dates specified for follow-up
//                return today.getDate() == followUp.getDate().getDate() ||
//                        today.getDate() == followUp.getDate().getDate() + 15;
//            case ALTERNATE_MONTH:
//                // Check if the month difference between today and follow-up date is odd
//                return (today.getMonth() - followUp.getDate().getMonth()) % 2 != 0;
//            case QUARTERLY:
//                // Check if today's month is one of the specified months for follow-up
//                return (today.getMonth() % 3) == (followUp.getDate().getMonth() % 3);
//            case BIANNUALLY:
//                // Check if today's month is one of the specified months for follow-up
//                return (today.getMonth() % 6) == (followUp.getDate().getMonth() % 6);
//            case ANNUALLY:
//                // Check if today's month and day match the follow-up date's month and day
//                return today.getMonth() == followUp.getDate().getMonth() &&
//                        today.getDate() == followUp.getDate().getDate();
//            default:
//                return false;
//        }
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

//    public void sendCredentialsByEmail(String email, String username, String password) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//        helper.setTo(email);
//        helper.setSubject("Welcome to Zencare - Your Credentials");
//        String emailBody = "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "    <title>Zencare - Your Credentials</title>\n" +
//                "</head>\n" +
//                "<body style=\"font-family: Arial, sans-serif;\">\n" +
//                "    <div style=\"background-color: #f5f5f5; padding: 20px; border-radius: 10px;\">\n" +
//                "        <h1 style=\"color: #333333;\">Welcome to Zencare!</h1>\n" +
//                "        <p style=\"color: #666666;\">Below are your login credentials:</p>\n" +
//                "        <ul>\n" +
//                "            <li><strong>Username:</strong> " + username + "</li>\n" +
//                "            <li><strong>Password:</strong> " + password + "</li>\n" +
//                "        </ul>\n" +
//                "        <p style=\"color: #666666;\">Please keep your credentials secure and do not share them with anyone.</p>\n" +
//                "        <p style=\"color: #666666;\">If you have any questions or need assistance, feel free to contact our support team.</p>\n" +
//                "        <p style=\"color: #666666;\">Best regards,<br>Zencare Team</p>\n" +
//                "    </div>\n" +
//                "</body>\n" +
//                "</html>";
//        helper.setText(emailBody, true);
//        helper.setFrom("noreply@zencare.com");
//        javaMailSender.send(mimeMessage);
//    }


//    private String generateUniqueUsername() {
//        String generatedUsername = null;
//        boolean isUnique = false;
//        while (!isUnique) {
//            // Generate a username starting with "FHW" followed by a sequence of numbers
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


//    public FieldHealthcareWorkerDTO getFieldHealthcareWorkerByUsername(String username) {
//        FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(username)
//                .orElseThrow(() -> new DoctorNotFoundException("Field Health Care Worker not found with username: " + username));
//        if (worker == null) {
//            // Handle the case where no worker is found with the provided username
//            System.out.println("Hello\n");
//            return null;
//        }
//        return convertToDTO2(worker);
//    }