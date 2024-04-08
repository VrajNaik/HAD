package com.Team12.HADBackEnd.services.Doctor;

import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorUpdateRequestDTO;
import com.Team12.HADBackEnd.models.*;

import com.Team12.HADBackEnd.payload.exception.*;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.repository.*;

import com.Team12.HADBackEnd.util.CredentialGenerator.CredentialService;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import com.Team12.HADBackEnd.util.MailService.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


import org.springframework.transaction.annotation.Transactional;

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
    private FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private HealthRecordRepository healthRecordRepository;
    @Autowired
    private FollowUpRepository followUpRepository;
    @Autowired
    private ICD10CodeRepository icd10CodeRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private DTOConverter dtoConverter;


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
        return convertToDTO(updatedDoctor);
    }
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
//        doctor.setCitizens(districtRepository.findById(doctor.getDistrictId()));
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

    public List<DoctorForAdminDTO> getAllDoctorsWithDistricts() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(dtoConverter::convertToDoctorForAdminDTO)
                .collect(Collectors.toList());
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

        // Set DistrictDTO if doctor has a district
        if (doctor.getDistrict() != null) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setId(doctor.getDistrict().getId());
            districtDTO.setName(doctor.getDistrict().getName());
            doctorDTO.setDistrict(districtDTO);
        }

        return doctorDTO;
    }

    public CitizenDTO getCitizenByAbhaId(String abhaId) {
        Citizen citizen = citizenRepository.findByAbhaId(abhaId);
        if (citizen != null) {
            return mapToCitizenDTO(citizen);
        }
        return null;
    }


    public List<CitizenDTO> getCitizensByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        List<Citizen> citizens = citizenRepository.findByDoctor(doctor);

        return citizens.stream()
                .map(this::mapToCitizenDTO)
                .collect(Collectors.toList());
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
        citizenDTO.setDistrict(citizen.getDistrict().getName() );
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
        HealthRecord healthRecord = citizen.getHealthRecord();
        if (healthRecord != null) {
            HealthRecordDTO healthRecordDTO = new HealthRecordDTO();
            healthRecordDTO.setId(healthRecord.getId());
            healthRecordDTO.setPrescriptions(healthRecord.getPrescriptions());
            healthRecordDTO.setConclusion(healthRecord.getConclusion());
            healthRecordDTO.setDiagnosis(healthRecord.getDiagnosis());
            healthRecordDTO.setTimestamp(healthRecord.getTimestamp());
            healthRecordDTO.setStatus(healthRecord.getStatus());
            citizenDTO.setHealthRecordDTO(healthRecordDTO);

        }
        if (citizen.getHealthRecord() != null && citizen.getHealthRecord().getFollowUps() != null) {
            List<FollowUpDTO> followUpsDTO = citizen.getHealthRecord().getFollowUps().stream()
                    .map(this::convertToFollowUpDTO)
                    .collect(Collectors.toList());
            HealthRecordDTO healthRecordDTO = citizenDTO.getHealthRecordDTO();
            healthRecordDTO.setFollowUps(followUpsDTO);
        }
        if (citizen.getHealthRecord() != null && citizen.getHealthRecord().getIcd10Codes() != null) {
            List<ICDCodesDTO> icdCodesDTO = citizen.getHealthRecord().getIcd10Codes().stream()
                    .map(this::convertToICD10CodeDTO)
                    .collect(Collectors.toList());
            HealthRecordDTO healthRecordDTO = citizenDTO.getHealthRecordDTO();
            healthRecordDTO.setIcd10codes(icdCodesDTO);
        }

        return citizenDTO;
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

    public HealthRecordDTO createHealthRecord(HealthRecordCreationDTO healthRecordCreationDTO) {

        Citizen citizen = citizenRepository.findById(healthRecordCreationDTO.getCitizenId())
                .orElseThrow(() -> new RuntimeException("Citizen not found with ID: " + healthRecordCreationDTO.getCitizenId()));

        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(healthRecordCreationDTO.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + healthRecordCreationDTO.getWorkerId()));

        Doctor doctor = doctorRepository.findById(healthRecordCreationDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + healthRecordCreationDTO.getDoctorId()));

//        ICD10Code icd10Code = icd10CodeRepository.findById(healthRecordCreationDTO.getIcd10CodeId())
//                .orElseThrow(() -> new RuntimeException("ICD-10 code not found with ID: " + healthRecordCreationDTO.getIcd10CodeId()));

        List<ICD10Code> icd10Codes = icd10CodeRepository.findAllById(healthRecordCreationDTO.getIcd10CodeId());
        List<String> prescriptions = Collections.singletonList(healthRecordCreationDTO.getPrescription());

        HealthRecord healthRecord = new HealthRecord();
        healthRecord.setCitizen(citizen);
        healthRecord.setFieldHealthCareWorker(fieldHealthCareWorker);
        healthRecord.setDoctor(doctor);
        healthRecord.setPrescriptions(prescriptions);
        healthRecord.setConclusion(healthRecordCreationDTO.getConclusion());
        healthRecord.setDiagnosis(healthRecordCreationDTO.getDiagnosis());
        healthRecord.setTimestamp(healthRecordCreationDTO.getTimestamp());
        healthRecord.setIcd10Codes(icd10Codes);

        HealthRecord savedHealthRecord = healthRecordRepository.save(healthRecord);

        return convertToDTO(savedHealthRecord);
    }

    private HealthRecordDTO convertToDTO(HealthRecord healthRecord) {
        HealthRecordDTO dto = new HealthRecordDTO();
        dto.setId(healthRecord.getId());
        dto.setPrescriptions(healthRecord.getPrescriptions());
        dto.setConclusion(healthRecord.getConclusion());
        dto.setDiagnosis(healthRecord.getDiagnosis());
        dto.setTimestamp(healthRecord.getTimestamp());
        dto.setStatus(healthRecord.getStatus());
        Doctor doctor = healthRecord.getDoctor();
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
            dto.setDoctorDTO(doctorDTO);
        }
        FieldHealthCareWorker fieldHealthCareWorker = healthRecord.getFieldHealthCareWorker();
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
            dto.setFieldHealthCareWorker(workerDTO);
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
        Citizen citizen = healthRecord.getCitizen();
        CitizenDTO citizenDTO = mapToCitizenDTO(citizen);
        dto.setIcd10codes(healthRecord.getIcd10Codes().stream()
                .map(this::convertToICD10CodeDTO)
                .collect(Collectors.toList()));
        dto.setCitizenDTO(citizenDTO);
        return dto;
    }
    private ICDCodesDTO convertToICD10CodeDTO(ICD10Code icd10Code) {
        ICDCodesDTO icd10CodeDTO = new ICDCodesDTO();
        icd10CodeDTO.setId(icd10Code.getId());
        icd10CodeDTO.setCode(icd10Code.getCode());
        icd10CodeDTO.setName(icd10Code.getName());
        icd10CodeDTO.setDescription(icd10Code.getDescription());
        return icd10CodeDTO;
    }

    public HealthRecordDTO addPrescriptionToHealthRecord(PrescriptionDTO prescriptionDTO) {
        HealthRecord healthRecord = healthRecordRepository.findById(prescriptionDTO.getHealthRecordId())
                .orElseThrow(() -> new RuntimeException("Health record not found with ID: " + prescriptionDTO.getHealthRecordId()));

        List<String> prescriptions = healthRecord.getPrescriptions();
        if (prescriptions == null) {
            prescriptions = new ArrayList<>();
        }
        prescriptions.add(prescriptionDTO.getPrescription());
        healthRecord.setPrescriptions(prescriptions);

        HealthRecord updatedHealthRecord = healthRecordRepository.save(healthRecord);
        return convertToDTO(updatedHealthRecord);
    }

    public HealthRecordDTO editLastPrescription(PrescriptionDTO editPrescriptionDTO) {
        HealthRecord healthRecord = healthRecordRepository.findById(editPrescriptionDTO.getHealthRecordId())
                .orElseThrow(() -> new RuntimeException("Health record not found with ID: " + editPrescriptionDTO.getHealthRecordId()));

        // Retrieve or initialize prescriptions list
        List<String> prescriptions = healthRecord.getPrescriptions();
        if (prescriptions == null || prescriptions.isEmpty()) {
            throw new RuntimeException("No prescriptions found for the health record");
        }

        // Update the last prescription
        int lastIndex = prescriptions.size() - 1;
        prescriptions.set(lastIndex, editPrescriptionDTO.getPrescription());
        healthRecord.setPrescriptions(prescriptions);

        HealthRecord updatedHealthRecord = healthRecordRepository.save(healthRecord);
        return convertToDTO(updatedHealthRecord);
    }

    public FollowUpDTO createFollowUp(FollowUpCreationDTO followUpRequestDTO) {
        // Fetch the health record by ID
        HealthRecord healthRecord = healthRecordRepository.findById(followUpRequestDTO.getHealthRecordId())
                .orElseThrow(() -> new RuntimeException("Health record not found with ID: " + followUpRequestDTO.getHealthRecordId()));

        // Fetch the field healthcare worker by ID
        FieldHealthCareWorker fieldHealthCareWorker = fieldHealthCareWorkerRepository.findById(followUpRequestDTO.getFieldHealthCareWorkerId())
                .orElseThrow(() -> new RuntimeException("Field healthcare worker not found with ID: " + followUpRequestDTO.getFieldHealthCareWorkerId()));

        // Create a new follow-up entity
        FollowUp followUp = new FollowUp();
        followUp.setHealthRecord(healthRecord);
        followUp.setFieldHealthCareWorker(fieldHealthCareWorker);
        followUp.setDate(followUpRequestDTO.getDate());
        followUp.setStatus(followUpRequestDTO.getStatus());
        followUp.setInstructions(followUpRequestDTO.getInstructions());
        followUp.setMeasureOfVitals(followUpRequestDTO.getMeasureOfVitals());

        FollowUp savedFollowUp = followUpRepository.save(followUp);

        Citizen citizen = healthRecord.getCitizen();
        citizen.setStatus("ongoing");
        citizenRepository.save(citizen);

        return convertToDTO(savedFollowUp);
    }

    private FollowUpDTO convertToDTO(FollowUp followUp) {
        FollowUpDTO followUpDTO = new FollowUpDTO();
        followUpDTO.setId(followUp.getId());
        followUpDTO.setDate(followUp.getDate());
        followUpDTO.setStatus(followUp.getStatus());
        followUpDTO.setInstructions(followUp.getInstructions());
        followUpDTO.setMeasureOfVitals(followUp.getMeasureOfVitals());
        FieldHealthCareWorker fieldHealthCareWorker = followUp.getFieldHealthCareWorker();
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
            followUpDTO.setFieldHealthCareWorker(workerDTO);
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
        HealthRecord healthRecord = followUp.getHealthRecord();
        if(healthRecord != null) {
            HealthRecordDTO healthRecordDTO = convertToDTO(healthRecord);
            followUpDTO.setHealthRecord(healthRecordDTO);
        }
        return followUpDTO;
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

    public DoctorDTO getDoctorByUsername(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Dcotor not found with username: " + username));
        return convertToDTO(doctor);
    }

    public void addFollowUp(FollowUpCreationByDoctorDTO followUpDTO) {
        FollowUp followUp = new FollowUp();
        Long healthRecordId = followUpDTO.getHealthRecordId();
        if(healthRecordId != null) {
            HealthRecord healthRecord = healthRecordRepository.findById(healthRecordId)
                    .orElseThrow(() -> new NotFoundException("HealthRecord not found with id: " + healthRecordId));
            followUp.setHealthRecord(healthRecord);
        }
        Long workerId = followUpDTO.getFieldHealthCareWorkerId();
        if(workerId != null) {
            FieldHealthCareWorker worker = fieldHealthCareWorkerRepository.findById(workerId)
                    .orElseThrow(() -> new NotFoundException("Field Health care Worker not found with id: " + healthRecordId));
            followUp.setFieldHealthCareWorker(worker);
        }
        followUp.setDate(followUpDTO.getScheduledDateTime());
        followUp.setFrequency(followUpDTO.getFrequency());
        followUp.setRecurrenceStartTime(new Date());
        followUp.setRecurrenceEndTime(followUpDTO.getRecurrenceEndTime());

        followUpRepository.save(followUp);
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


//    public ResponseEntity<Doctor> updateDoctor(DoctorUpdateRequest request) {
//        return doctorRepository.findById(request.getId()).map(doctor -> {
//            doctor.setName(request.getName());
//            doctor.setLicenseId(request.getLicenseId());
//            doctor.setAge(request.getAge());
//            doctor.setGender(request.getGender());
//            doctor.setSpecialty(request.getSpecialty());
//            doctor.setPhoneNum(request.getPhoneNum());
//            doctor.setEmail(request.getEmail());
//            doctor.setUsername(request.getUsername());
//            doctor.setPassword(request.getPassword());
//            Doctor savedDoctor = doctorRepository.save(doctor);
//            return ResponseEntity.ok().body(savedDoctor);
//        }).orElse(ResponseEntity.notFound().build());
//    }


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


//2

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
//        // Map Doctor to DoctorDTO
//        Doctor doctor = citizen.getDoctor();
//        if (doctor != null) {
//            DoctorDTO doctorDTO = new DoctorDTO();
//            doctorDTO.setId(doctor.getId());
//            doctorDTO.setName(doctor.getName());
//            // Map other fields as needed
//            citizenDTO.setDoctorDTO(doctorDTO);
//        }
//
//        return citizenDTO;
//    }


//    @Transactional
//    public void setActiveStatusByUsername(String username, boolean active) {
//        Doctor doctor = doctorRepository.findByUsername(username)
//                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with username: " + username));
//
//        if (doctor.isActive() == active) {
//            throw new DoctorAlreadyDeactivatedException("Doctor is already " + (active ? "activated" : "deactivated"));
//        }
//
//        doctor.setActive(active);
//        doctorRepository.save(doctor);
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
//        user.setActive(active);
//        userRepository.save(user);
//    }



//    private HealthRecordDTO convertToDTO(HealthRecord healthRecord) {
//        HealthRecordDTO dto = new HealthRecordDTO();
//        dto.setId(healthRecord.getId());
//        dto.setCitizenId(healthRecord.getCitizen().getId());
//        dto.setWorkerId(healthRecord.getFieldHealthCareWorker().getId());
//        dto.setDoctorId(healthRecord.getDoctor().getId());
//        dto.setPrescription(healthRecord.getPrescription());
//        dto.setConclusion(healthRecord.getConclusion());
//        dto.setDiagnosis(healthRecord.getDiagnosis());
//        dto.setTimestamp(healthRecord.getTimestamp());
//        return dto;
//    }

//    public HealthRecordDTO addPrescriptionToHealthRecord(PrescriptionDTO prescriptionDTO) {
//        HealthRecord healthRecord = healthRecordRepository.findById(prescriptionDTO.getHealthRecordId())
//                .orElseThrow(() -> new RuntimeException("Health record not found with ID: " + prescriptionDTO.getHealthRecordId()));
//
//        // Add prescription to the existing health record
//        healthRecord.setPrescription(prescriptionDTO.getPrescription());
//
//        HealthRecord updatedHealthRecord = healthRecordRepository.save(healthRecord);
//        return convertToDTO(updatedHealthRecord);
//    }
//
//    public HealthRecordDTO editPrescription(PrescriptionDTO editPrescriptionDTO) {
//        HealthRecord healthRecord = healthRecordRepository.findById(editPrescriptionDTO.getHealthRecordId())
//                .orElseThrow(() -> new RuntimeException("Health record not found with ID: " + editPrescriptionDTO.getHealthRecordId()));
//
//        // Append the new prescription to the existing prescription
//        String currentPrescription = healthRecord.getPrescription();
//        String newPrescription = editPrescriptionDTO.getPrescription();
//        String updatedPrescription = currentPrescription + "\n" + newPrescription; // Append new prescription to the current one
//        healthRecord.setPrescription(updatedPrescription);
//
//        HealthRecord updatedHealthRecord = healthRecordRepository.save(healthRecord);
//        return convertToDTO(updatedHealthRecord);
//    }

//    private void sendCredentialsByEmail(String email, String username, String password) {
//        // Prepare email message
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(email);
//        mailMessage.setSubject("Credentials for accessing the system");
//        mailMessage.setText("Your username: " + username + "\nYour password: " + password);
//
//        javaMailSender.send(mailMessage);
//    }


//private String generateUniqueUsername() {
//    String generatedUsername = null;
//    boolean isUnique = false;
//    while (!isUnique) {
//        // Generate a username starting with "DR" followed by a sequence of numbers
//        int randomNumber = new Random().nextInt(90000) + 10000; // Generates a random 5-digit number
//        generatedUsername = "DR" + randomNumber;
//        isUnique = !doctorRepository.existsByUsername(generatedUsername);
//    }
//    return generatedUsername;
//}
//private String generateRandomPassword() {
//    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//    StringBuilder password = new StringBuilder();
//    Random rnd = new Random();
//    while (password.length() < 10) { // length of the random string.
//        int index = (int) (rnd.nextFloat() * characters.length());
//        password.append(characters.charAt(index));
//    }
//    return password.toString();
//}