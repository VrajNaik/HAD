package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenRegistrationDTO;
import com.Team12.HADBackEnd.DTOs.Citizen.CitizensRegistrationDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.AssignDoctorListRequest;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.AssignDoctorRequest;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.UpdateFollowUpStatusRequest;
import com.Team12.HADBackEnd.DTOs.Questionnaire.AnswersDTO;
import com.Team12.HADBackEnd.DTOs.Questionnaire.QuestionnaireResponseDTO;
import com.Team12.HADBackEnd.DTOs.Response.ResponseDTO;
import com.Team12.HADBackEnd.DTOs.Response.ResponseListDTO;
import com.Team12.HADBackEnd.models.FollowUp;
import com.Team12.HADBackEnd.models.Response;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;

import com.Team12.HADBackEnd.services.BlackBox.Questionnaire.QuestionnaireService;
import com.Team12.HADBackEnd.services.BlackBox.smsService.SendSmsForFollowUp;
import com.Team12.HADBackEnd.services.Translation.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Team12.HADBackEnd.services.FieldHealthCareWorker.FieldHealthCareWorkerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/FieldHealthCareWorker")
public class FieldHealthCareWorkerController {


    private final SendSmsForFollowUp sendSmsForFollowUp;

    private final FieldHealthCareWorkerService fieldHealthCareWorkerService;

    private final UserRepository userRepository;

    private final QuestionnaireService questionnaireService;

    private final TranslationService translationService;

    @Autowired
    public FieldHealthCareWorkerController(FieldHealthCareWorkerService fieldHealthCareWorkerService,
                                           UserRepository userRepository,
                                           SendSmsForFollowUp sendSmsForFollowUp,
                                           QuestionnaireService questionnaireService,
                                           TranslationService translationService) {
        this.fieldHealthCareWorkerService = fieldHealthCareWorkerService;
        this.userRepository = userRepository;
        this.sendSmsForFollowUp = sendSmsForFollowUp;
        this.questionnaireService = questionnaireService;
        this.translationService = translationService;
    }


    @PostMapping("/registerCitizen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> registerCitizen(@RequestBody CitizenRegistrationDTO citizen) {
        return fieldHealthCareWorkerService.registerCitizen(citizen);
    }

    @PostMapping("/registerCitizens")
//
    public ResponseEntity<?> registerCitizens(@RequestBody CitizensRegistrationDTO citizens) {
        return fieldHealthCareWorkerService.registerCitizens(citizens);
    }

//    @PostMapping("/registerCitizenNew")
//    public ResponseEntity<?> registerCitizenNew(@RequestBody CitizenRegistrationDTO citizen) {
//        // Translate data to English based on the provided language
//        CitizenRegistrationDTO englishCitizen = translationService.translateToEnglish(citizen);
//
//        // Save the translated data to the database
//        fieldHealthCareWorkerService.registerCitizen(englishCitizen);
//
//        return ResponseEntity.ok("Citizen registration data saved successfully!");
//    }

    @GetMapping("/getQuestionnaire")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getQuestionnaireById(@RequestParam Long id) {
        QuestionnaireResponseDTO questionnaireResponse = questionnaireService.getQuestionnaireById(id);
        return ResponseEntity.ok(questionnaireResponse);
    }


    @PostMapping("/getAssessmentScore")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> calculateScore(@RequestBody AnswersDTO answersDTO) {
        try {
            int score = fieldHealthCareWorkerService.calculateScore(answersDTO.getAnswers());
            return  ResponseMessage.createSuccessResponse(HttpStatus.OK, "Score for the Citizen is: " + score);
        } catch (IllegalArgumentException e) {
            return  ResponseMessage.createNotSuccessResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @GetMapping("/getDoctorsByDistID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getDoctorsByFHWUsername(@RequestParam String username) {
        return fieldHealthCareWorkerService.getDoctorsByFHWUsername(username);
    }


    @GetMapping("/getFHWByDistrictId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FieldHealthCareWorkerWithHealthRecordDTO>> getFieldHealthCareWorkersInDistrict(@RequestParam Long districtId) {
        List<FieldHealthCareWorkerWithHealthRecordDTO> unassignedWorkers = fieldHealthCareWorkerService.getFieldHealthCareWorkerDTOs(districtId);
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }


    @GetMapping("/getFollowUpsForToday")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public List<FollowUpReturnDTO> getFollowUpsForToday(@RequestParam String username) {
        return fieldHealthCareWorkerService.getFollowUpsForToday(username);
    }


    @PostMapping("/getHealthRecordById")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getHealthRecordByCitizenId(@RequestParam Long citizenId) {
        return fieldHealthCareWorkerService.getHealthRecordByCitizenId(citizenId);
    }


    @GetMapping("/getFollowUpsByHealthRecordId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getFollowUpsByHealthRecordId(@RequestParam Long healthRecordId) {
        return fieldHealthCareWorkerService.getFollowUpsByHealthRecordId(healthRecordId);
    }


    @PostMapping("/updateFollowUpStatus")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> updateFollowUpStatus(@RequestBody UpdateFollowUpStatusRequest request) {
        try {
            fieldHealthCareWorkerService.updateFollowUpStatus(request.getFollowUpId(), request.getStatus());
            return ResponseEntity.ok(new MessageResponse("Follow-up status updated successfully."));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }


    @GetMapping("/getByUsername")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> getFieldHealthcareWorkerByUsername(@RequestParam String username) {
        FieldHealthCareWorkerWithHealthRecordDTO fhwDTO = fieldHealthCareWorkerService.getFieldHealthcareWorkerByUsername(username);
        return ResponseEntity.ok(fhwDTO);
    }


    private static final Logger logger = LoggerFactory.getLogger(FieldHealthCareWorkerController.class);

    @GetMapping("/sendFollowUpSms")
    public void sendMessage(@RequestParam("language") String language) {
        logger.info("Scheduled task started at {}", java.time.LocalTime.now());
        try {
            sendSmsForFollowUp.sendMessage(language);
            logger.info("Scheduled task completed successfully at {}", java.time.LocalTime.now());
        } catch (Exception e) {
            logger.error("Scheduled task failed with error: {}", e.getMessage());
        }
    }


    @GetMapping("/isLastFollowUp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> isLastFollowUp(@RequestParam String abhaId) {
        Optional<FollowUp> followUpOptional = fieldHealthCareWorkerService.findAssignedFollowUpByAbhaId(abhaId);
        if (followUpOptional.isPresent()) {
            return ResponseEntity.ok(followUpOptional.get().getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


//    @PostMapping("/addResponse")
//    public ResponseEntity<?> addResponse(@RequestBody ResponseDTO responseDTO) {
//        Response savedResponse = fieldHealthCareWorkerService.addResponse(responseDTO);
//        return new ResponseEntity<>(savedResponse, HttpStatus.CREATED);
//    }
    @PostMapping("/addResponse")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> addResponse(@RequestBody ResponseDTO responseDTO) {
        return fieldHealthCareWorkerService.addResponse(responseDTO);
    }

    @GetMapping("/getHospitals")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getHospitals(@RequestParam String username) {
        return fieldHealthCareWorkerService.getHospitalsInDistrict(username);
    }

    @PostMapping("/addResponses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> addResponses(@RequestBody ResponseListDTO response) {
        return fieldHealthCareWorkerService.addResponses(response.getResponses());
    }

    @PostMapping("/assignDoctorToCitizen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> assignDoctorToCitizen(@RequestBody AssignDoctorRequest request) {
//        boolean success = fieldHealthCareWorkerService.assignDoctorToCitizen(request.getAbhaId(), request.getDoctorUsername());
//
//        if (success) {
//            return ResponseEntity.ok("Doctor assigned to Citizen successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to assign Doctor to Citizen");
//        }
        return fieldHealthCareWorkerService.assignDoctorToCitizen(request.getAbhaId(), request.getDoctorUsername());
    }

    @PostMapping("/assignDoctorsToCitizens")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> assignDoctorsToCitizens(@RequestBody AssignDoctorListRequest request) {
//        boolean success = fieldHealthCareWorkerService.assignDoctorsToCitizens(request.getDoctorAssignments());
//
//        if (success) {
//            return ResponseEntity.ok("Doctors assigned to citizens successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to assign doctors to citizens");
//        }
        return fieldHealthCareWorkerService.assignDoctorsToCitizens(request.getDoctorAssignments());
    }


    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateFeildHealthCareWorker(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            fieldHealthCareWorkerService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorAlreadyDeactivatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateFeildHealthCareWorker(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            fieldHealthCareWorkerService.setActiveStatusByUsername(usernameDTO.getUsername(), true);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorAlreadyDeactivatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


//@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RequestMapping("/FieldHealthCareWorker")
//public class FieldHealthCareWorkerController {
//    @PostMapping("/addFieldHealthCareWorker")
//    @PreAuthorize("hasRole('ADMIN')")
//    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker fieldHealthCareWorker) {
//        return fieldHealthCareWorkerService.addFieldHealthCareWorker(fieldHealthCareWorker);
//    }

//    @GetMapping("/viewFieldHealthCareWorkers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FieldHealthCareWorker>> getAllFieldHealthCareWorker() {
//        List<FieldHealthCareWorker> fieldHealthCareWorker = fieldHealthCareWorkerService.getAllFieldHealthCareWorker();
//        return new ResponseEntity<>(fieldHealthCareWorker, HttpStatus.OK);
//    }



//    @PostMapping("/getByUsername")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<FieldHealthcareWorkerDTO> getFieldHealthcareWorkerByUsername(@RequestBody UsernameDTO usernameRequest) {
//        String username = usernameRequest.getUsername();
//        try {
//            FieldHealthcareWorkerDTO workerDTO = fieldHealthCareWorkerService.getFieldHealthcareWorkerByUsername(username);
//            if (workerDTO == null) {
//                // Handle the case where no worker is found with the provided username
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//            return new ResponseEntity<>(workerDTO, HttpStatus.OK);
//        } catch (Exception e) {
//            // Handle other exceptions here
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


//    @GetMapping("/getUnassignedFHW")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getUnassignedFieldHealthCareWorkers() {
//        List<FieldHealthcareWorkerDTO> unassignedWorkers = fieldHealthCareWorkerService.getUnassignedFieldHealthCareWorkerDTOs();
//        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
//    }



//    @PostMapping("/addFieldHealthCareWorker")
//    @PreAuthorize("hasRole('ADMIN')")
//    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker fieldHealthCareWorker) {
//        try{
//            return fieldHealthCareWorkerService.addFieldHealthCareWorker(fieldHealthCareWorker);
//        }
//        catch (DuplicateEmailIdException e) {
//            throw new AuthenticationServiceException(e.getMessage(), e);
//        }
//    }
//
//    @GetMapping("/viewFieldHealthCareWorkers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getAllFieldHealthCareWorker() {
//        List<FieldHealthcareWorkerDTO> worker = fieldHealthCareWorkerService.getAllFieldHealthCareWorkersWithDistricts();
//        return new ResponseEntity<>(worker, HttpStatus.OK);
//    }
//
//    @PutMapping("/updateFieldHealthCareWorker")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<FieldHealthcareWorkerDTO> updateDoctor(@RequestBody SupervisorUpdateRequestDTO request) {
//        FieldHealthcareWorkerDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
//        return ResponseEntity.ok(updatedWorkerDTO);
//    }



//        try {
//            List<FollowUpDTO> followUpDTOs = followUpService.getFollowUpsByHealthRecordId(requestDTO);
//            return ResponseEntity.ok(followUpDTOs);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
//        }


//        try {
//            HealthRecordDTO healthRecordDTO = fieldHealthCareWorkerService.getHealthRecordByCitizenId(requestDTO.getCitizenId());
//            return ResponseEntity.ok(healthRecordDTO);
//        } catch (HealthRecordNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
//        }



//    @PostMapping("/addFieldHealthCareWorker")
//    @PreAuthorize("hasRole('ADMIN')")
//    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker fieldHealthCareWorker) {
//        try{
//            return fieldHealthCareWorkerService.addFieldHealthCareWorker(fieldHealthCareWorker);
//        }
//        catch (DuplicateEmailIdException e) {
//            throw new AuthenticationServiceException(e.getMessage(), e);
//        }
//    }
//
//    @GetMapping("/viewFieldHealthCareWorkers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getAllFieldHealthCareWorker() {
//        List<FieldHealthcareWorkerDTO> worker = fieldHealthCareWorkerService.getAllFieldHealthCareWorkersWithDistricts();
//        return new ResponseEntity<>(worker, HttpStatus.OK);
//    }
//
//    @PutMapping("/updateFieldHealthCareWorker")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<FieldHealthcareWorkerDTO> updateDoctor(@RequestBody SupervisorUpdateRequestDTO request) {
//        FieldHealthcareWorkerDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
//        return ResponseEntity.ok(updatedWorkerDTO);
//    }


// registerCitizen

//        CitizenDTO registeredCitizen = fieldHealthCareWorkerService.registerCitizen(citizen);
//        return new ResponseEntity<>(registeredCitizen, HttpStatus.CREATED);


// getAssessmentScore

//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        return ResponseEntity.ok("Score: " + score);

// getDoctorsByDistID

//        String username = usernameDTO.getUsername();

