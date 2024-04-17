package com.Team12.HADBackEnd.services.Doctor;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpCreationByDoctorDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordCreationDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordUpdateDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.PrescriptionDTO;
import com.Team12.HADBackEnd.DTOs.Response.ResponseDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.*;
import com.Team12.HADBackEnd.DTOs.District.DistrictDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.payload.response.ResponseMessage;
import com.Team12.HADBackEnd.repository.*;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final DistrictRepository districtRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    private final CitizenRepository citizenRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final FollowUpRepository followUpRepository;
    private final ICD10CodeRepository icd10CodeRepository;
    private final CredentialService credentialService;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final DTOConverter dtoConverter;
    private final ResponseRepository responseRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DistrictRepository districtRepository,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                             CitizenRepository citizenRepository,
                             HealthRecordRepository healthRecordRepository,
                             FollowUpRepository followUpRepository,
                             ICD10CodeRepository icd10CodeRepository,
                             CredentialService credentialService,
                             EmailService emailService,
                             PasswordEncoder passwordEncoder,
                             DTOConverter dtoConverter, ResponseRepository responseRepository) {
        this.doctorRepository = doctorRepository;
        this.districtRepository = districtRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.fieldHealthCareWorkerRepository = fieldHealthCareWorkerRepository;
        this.citizenRepository = citizenRepository;
        this.healthRecordRepository = healthRecordRepository;
        this.followUpRepository = followUpRepository;
        this.icd10CodeRepository = icd10CodeRepository;
        this.credentialService = credentialService;
        this.emailService = emailService;
        this.encoder = passwordEncoder;
        this.dtoConverter = dtoConverter;
        this.responseRepository = responseRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorDTO updateDoctor(DoctorUpdateRequestDTO request) {
        Doctor doctor = doctorRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Doctor not found with username: " + request.getUsername()));

        if (doctorRepository.existsByEmail(request.getEmail()) && !Objects.equals(doctor.getEmail(), request.getEmail())) {
            throw new DuplicateEmailIdException("Doctor with the same Email ID already exists.");
        }
        if (doctorRepository.existsByLicenseId(request.getLicenseId()) && !Objects.equals(doctor.getLicenseId(), request.getLicenseId())) {
            throw new DuplicateLicenseIdException("Doctor with the same license ID already exists.");
        }
        if (doctorRepository.existsByPhoneNum(request.getPhoneNum()) && !Objects.equals(doctor.getPhoneNum(), request.getPhoneNum())) {
            throw new DuplicateEmailIdException("Doctor with the same Phone Number already exists.");
        }
        if (request.getName() != null) {
            doctor.setName(request.getName());
        }
        if (request.getLicenseId() != null) {
            doctor.setLicenseId(request.getLicenseId());
        }
        if (request.getAge() != 0) {
            doctor.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            doctor.setGender(request.getGender());
        }
        if (request.getSpecialty() != null) {
            doctor.setSpecialty(request.getSpecialty());
        }
        if (request.getPhoneNum() != null) {
            doctor.setPhoneNum(request.getPhoneNum());
        }
        if (request.getEmail() != null) {
            doctor.setEmail(request.getEmail());
        }
        if (request.getDistrict().getId() != null) {
            District newDistrict = districtRepository.findById(request.getDistrict().getId())
                    .orElseThrow(() -> new RuntimeException("District not found with id: " + request.getDistrict().getId()));
            doctor.setDistrict(newDistrict);
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return dtoConverter.convertToDTO(updatedDoctor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Doctor addDoctor(Doctor doctor) throws DuplicateLicenseIdException, DuplicateEmailIdException {
        String generatedUsername = credentialService.generateUniqueUsername("doctor");
        String generatedRandomPassword = credentialService.generateRandomPassword();

        System.out.println("Received Doctor object: " + doctor);

        District district = doctor.getDistrict();
        System.out.println("Associated District object: " + district);

        User user = new User(generatedUsername,
                doctor.getEmail(),
                encoder.encode(generatedRandomPassword));

        Set<Role> roles = new HashSet<>();
        Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                .orElseThrow(() -> new RuntimeException("Error: DOCTOR role not found."));
        roles.add(doctorRole);
        user.setRoles(roles);
        userRepository.save(user);

        if (doctorRepository.existsByLicenseId(doctor.getLicenseId())) {
            throw new DuplicateLicenseIdException("Doctor with the same license ID already exists.");
        }

        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DuplicateEmailIdException("Doctor with the same Email ID already exists.");
        }
        if (doctorRepository.existsByPhoneNum(doctor.getPhoneNum())) {
            throw new DuplicateEmailIdException("Doctor with the same Phone Number already exists.");
        }

        doctor.setUsername(generatedUsername);
        doctor.setPassword(encoder.encode(generatedRandomPassword));
        doctor.setDistrict(district);
        Doctor savedDoctor = doctorRepository.save(doctor);
        System.out.println("Doctor's District: " + savedDoctor.getDistrict());
        // 2
        System.out.println(generatedRandomPassword);
        try {
            emailService.sendCredentialsByEmail(savedDoctor.getEmail(), generatedUsername, generatedRandomPassword);
        }
        catch (MessagingException e) {
            System.out.println("Error");
        }
        return savedDoctor;
    }

    @Override
    public List<DoctorForAdminDTO> getAllDoctorsWithDistricts() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(dtoConverter::convertToDoctorForAdminDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorByUsername(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found with username: " + username));
        return dtoConverter.convertToDTO(doctor);
    }

    @Override
    public CitizenForDoctorDTO getCitizenByAbhaId(String abhaId) {
        Citizen citizen = citizenRepository.findByAbhaId(abhaId)
                .orElseThrow(() -> new NotFoundException("Citizen not found with ID: " + abhaId));
        return dtoConverter.convertToCitizenForDoctorDTO(citizen);
    }

    @Override
    public List<CitizenForDoctorDTO> getCitizensByDoctorId(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found with Username: " + username));

        List<Citizen> citizens = citizenRepository.findByDoctor(doctor);

        return citizens.stream()
                .map(dtoConverter::convertToCitizenForDoctorDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ResponseEntity<?> createHealthRecord(HealthRecordCreationDTO healthRecordCreationDTO) {

        Citizen citizen = citizenRepository.findByAbhaId(healthRecordCreationDTO.getAbhaId())
                .orElseThrow(() -> new NotFoundException("Citizen not found with ID: " + healthRecordCreationDTO.getAbhaId()));

        if (citizen.getHealthRecord() != null) {
            throw new NotFoundException("Health record already exists for citizen with ID: " + healthRecordCreationDTO.getAbhaId());
        }
        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findByUsername(healthRecordCreationDTO.getWorkerUsername())
                .orElseThrow(() -> new NotFoundException("Field healthcare worker not found with ID: " + healthRecordCreationDTO.getWorkerUsername()));

        Doctor doctor = doctorRepository.findByUsername(healthRecordCreationDTO.getDoctorUsername())
                .orElseThrow(() -> new NotFoundException("Doctor not found with ID: " + healthRecordCreationDTO.getDoctorUsername()));


        List<ICD10Code> icd10Codes = icd10CodeRepository.findAllById(healthRecordCreationDTO.getIcd10CodeId());
        List<String> prescriptions = Collections.singletonList(healthRecordCreationDTO.getPrescription());

        HealthRecord healthRecord = new HealthRecord();
        healthRecord.setCitizen(citizen);
        healthRecord.setFieldHealthCareWorker(fieldHealthCareWorker);
        healthRecord.setDoctor(doctor);
        healthRecord.setPrescriptions(prescriptions);
        healthRecord.setIcd10Codes(icd10Codes);
        if(healthRecordCreationDTO.getConclusion() != null) {
            healthRecord.setConclusion(healthRecordCreationDTO.getConclusion());
        }
        if(healthRecordCreationDTO.getDiagnosis() != null) {
            healthRecord.setDiagnosis(healthRecordCreationDTO.getDiagnosis());
        }
//        if(healthRecordCreationDTO.getTimestamp() != null) {
//            healthRecord.setTimestamp(healthRecordCreationDTO.getTimestamp());
//        }
        healthRecord.setTimestamp(new Date());

        healthRecordRepository.save(healthRecord);

        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Health record created successfully!");
    }


    @Override
    public ResponseEntity<?> updateHealthRecord(HealthRecordUpdateDTO healthRecordUpdateDTO) {
        Citizen citizen = citizenRepository.findByAbhaId(healthRecordUpdateDTO.getAbhaId())
                .orElseThrow(() -> new NotFoundException("Citizen not found with ID: " + healthRecordUpdateDTO.getAbhaId()));

        HealthRecord healthRecord = healthRecordRepository.findByCitizen_Id(citizen.getId())
                .orElseThrow(() -> new NotFoundException("Health record not found with ID: " + citizen.getId()));

        // Update fields that can be changed
        List<String> prescriptions = Collections.singletonList(healthRecordUpdateDTO.getPrescription());

        if (healthRecordUpdateDTO.getPrescription() != null) {
            healthRecord.setPrescriptions(prescriptions);
        }
        if (healthRecordUpdateDTO.getConclusion() != null) {
            healthRecord.setConclusion(healthRecordUpdateDTO.getConclusion());
        }
        if (healthRecordUpdateDTO.getDiagnosis() != null) {
            healthRecord.setDiagnosis(healthRecordUpdateDTO.getDiagnosis());
        }
        if (healthRecordUpdateDTO.getIcd10CodeIds() != null) {
            // Fetch new ICD10 codes by ID
            List<ICD10Code> newIcd10Codes = icd10CodeRepository.findAllById(healthRecordUpdateDTO.getIcd10CodeIds());

            // Set the new list of ICD10 codes to the health record, replacing the existing ones
            healthRecord.setIcd10Codes(new ArrayList<>(newIcd10Codes));
        }

        healthRecordRepository.save(healthRecord);
        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Health record created successfully!");
    }

    @Override
    public ResponseEntity<?> getResponseByABHAId(String abhaId) {
        Citizen citizen = citizenRepository.findByAbhaId(abhaId)
                .orElseThrow(() -> new NotFoundException("Citizen not found with ID: " + abhaId));

        Response response = responseRepository.findFirstByCitizenOrderByFollowUpNoDesc(citizen)
                .orElseThrow(() -> new NotFoundException("Response not found with ID: " + abhaId));

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setAbhaId(response.getCitizen().getAbhaId());
        responseDTO.setAnswers(response.getAnswers());
        responseDTO.setScore(response.getScore());
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<?> addPrescriptionToHealthRecord(PrescriptionDTO prescriptionDTO) {
        HealthRecord healthRecord = healthRecordRepository.findById(prescriptionDTO.getHealthRecordId())
                .orElseThrow(() -> new NotFoundException("Health record not found with ID: " + prescriptionDTO.getHealthRecordId()));

        List<String> prescriptions = healthRecord.getPrescriptions();
        if (prescriptions == null) {
            prescriptions = new ArrayList<>();
        }
        prescriptions.add(prescriptionDTO.getPrescription());
        healthRecord.setPrescriptions(prescriptions);

        healthRecordRepository.save(healthRecord);
//        return convertToDTO(updatedHealthRecord);
        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Prescription added successfully!");
    }

    @Override
    public ResponseEntity<?> editLastPrescription(PrescriptionDTO editPrescriptionDTO) {
        HealthRecord healthRecord = healthRecordRepository.findById(editPrescriptionDTO.getHealthRecordId())
                .orElseThrow(() -> new NotFoundException("Health record not found with ID: " + editPrescriptionDTO.getHealthRecordId()));

        List<String> prescriptions = healthRecord.getPrescriptions();
        if (prescriptions == null || prescriptions.isEmpty()) {
            throw new NotFoundException("No prescriptions found for the health record");
        }

        // Update the last prescription
        int lastIndex = prescriptions.size() - 1;
        prescriptions.set(lastIndex, editPrescriptionDTO.getPrescription());
        healthRecord.setPrescriptions(prescriptions);

        healthRecordRepository.save(healthRecord);
//        return convertToDTO(updatedHealthRecord);
        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Prescription Edited successfully!");
    }

    @Override
    public ResponseEntity<?> addFollowUp(FollowUpCreationByDoctorDTO followUpDTO) {
        FollowUp followUp = new FollowUp();
        Long healthRecordId = followUpDTO.getHealthRecordId();
        if(healthRecordId != null) {
            HealthRecord healthRecord = healthRecordRepository.findById(healthRecordId)
                    .orElseThrow(() -> new NotFoundException("HealthRecord not found with id: " + healthRecordId));
            List<FollowUp> assignedFollowUps = followUpRepository.findByHealthRecordAndStatus(healthRecord, "assigned");
            if(assignedFollowUps != null && !assignedFollowUps.isEmpty()) {
                throw new NotFoundException("Follow up already assigned to the follow up");
            }
            followUp.setHealthRecord(healthRecord);
        }
        String workerUsername = followUpDTO.getWorkerUsername();
        if(workerUsername != null) {
            FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findByUsername(workerUsername)
                    .orElseThrow(() -> new NotFoundException("Field Health care Worker not found with id: " + healthRecordId));
            followUp.setFieldHealthCareWorker(worker);
        }
        if (followUpDTO.getScheduledDateTime() != null) {
            followUp.setDate(followUpDTO.getScheduledDateTime());
        }
        if (followUpDTO.getFrequency() != null) {
            followUp.setFrequency(followUpDTO.getFrequency());
        }
        if (followUpDTO.getRecurrenceEndTime() != null) {
            followUp.setRecurrenceEndTime(followUpDTO.getRecurrenceEndTime());
        }
        if (followUpDTO.getInstructions() != null) {
            followUp.setInstructions(followUpDTO.getInstructions());
        }
        followUp.setStatus("Assigned");
        followUp.setRecurrenceStartTime(new Date());
        followUpRepository.save(followUp);
        return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "FollowUp Added successfully!");
    }

    @Transactional
    public void setActiveStatusByUsername(String username, boolean active) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found with username: " + username));
        if (doctor.isActive() == active) {
            throw new DoctorAlreadyDeactivatedException("Doctor is already " + (active ? "activated" : "deactivated"));
        }
        if (!doctor.getCitizens().isEmpty() || !doctor.getHealthRecords().isEmpty()) {
            StringBuilder message = new StringBuilder("Cannot deactivate doctor due to associated entities:\n");
            if (!doctor.getCitizens().isEmpty()) {
                message.append(" - This doctor is associated with citizens.\n");
            }
            if (!doctor.getHealthRecords().isEmpty()) {
                message.append(" - This doctor is associated with health records.\n");
            }
            throw new NotFoundException(message.toString());
        }
        doctor.setActive(active);
        doctorRepository.save(doctor);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        user.setActive(active);
        userRepository.save(user);
    }

    public DoctorDTO convertToDTO(Doctor doctor) {
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
}
