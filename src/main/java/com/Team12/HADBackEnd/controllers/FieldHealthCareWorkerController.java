package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.RoleNotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.security.services.smsService.SendSmsForFollowUp;
import com.Team12.HADBackEnd.twilio.TwilioMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/FieldHealthCareWorker")
public class FieldHealthCareWorkerController {

    @Autowired
    private FieldHealthCareWorkerService fieldHealthCareWorkerService;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    TwilioMessageService twilioMessageService;
    @Autowired
    private SendSmsForFollowUp sendSmsForFollowUp;


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
        } catch (RoleNotFoundException e) {
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
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorAlreadyDeactivatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/getByUsername")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> getFieldHealthcareWorkerByUsername(@RequestBody UsernameDTO usernameRequest) {
        String username = usernameRequest.getUsername();
        System.out.println(username);
        try {
            FieldHealthcareWorkerDTO workerDTO = fieldHealthCareWorkerService.getFieldHealthcareWorkerByUsername(username);
            if (workerDTO == null) {
                String message = "Field Health Care Worker Not Found with a given username";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
            }
            return ResponseEntity.ok(workerDTO);
        } catch (RoleNotFoundException ex) {
            String message = "Field Health Care Worker Not Found with a given username";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/getUnassignedFHW")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getUnassignedFieldHealthCareWorkers(@RequestBody DistrictIdRequestDTO request) {
        String username = request.getUsername();
        List<FieldHealthcareWorkerDTO> unassignedWorkers = fieldHealthCareWorkerService.getUnassignedFieldHealthCareWorkerDTOs(username);
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }

    @PostMapping("/getFHWByDistrictId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getFieldHealthCareWorkersInDistrict(@RequestBody DistrictIdDTO districtRequestDTO) {
        Long districtId = districtRequestDTO.getDistrictId();
        List<FieldHealthcareWorkerDTO> unassignedWorkers = fieldHealthCareWorkerService.getFieldHealthCareWorkerDTOs(districtId);
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }

    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<CitizenDTO> registerCitizen(@RequestBody CitizenRegistrationDTO citizen) {
        CitizenDTO registeredCitizen = fieldHealthCareWorkerService.registerCitizen(citizen);
        return new ResponseEntity<>(registeredCitizen, HttpStatus.CREATED);
    }
    @PostMapping("/calculateScore")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> calculateScore(@RequestBody AnswersDTO answersDTO) {
        try {
            int score = fieldHealthCareWorkerService.calculateScore(answersDTO.getAnswers());
            return ResponseEntity.ok("Score: " + score);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getDoctorsByDistID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getDoctorsByFHWUsername(@RequestBody UsernameDTO usernameDTO) {
        String username = usernameDTO.getUsername();
        return fieldHealthCareWorkerService.getDoctorsByFHWUsername(username);
    }

    @PostMapping("/updateFollowUpStatus")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> updateFollowUpStatus(@RequestBody UpdateFollowUpStatusRequest request) {
        try {
            fieldHealthCareWorkerService.updateFollowUpStatus(request.getFollowUpId(), request.getStatus());
            return ResponseEntity.ok(new MessageResponse("Follow-up status updated successfully."));
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
    }
    @PostMapping("/getHealthRecordById")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getHealthRecordByCitizenId(@RequestBody CitizenIdRequestDTO requestDTO) {
//        try {
//            HealthRecordDTO healthRecordDTO = fieldHealthCareWorkerService.getHealthRecordByCitizenId(requestDTO.getCitizenId());
//            return ResponseEntity.ok(healthRecordDTO);
//        } catch (HealthRecordNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
//        }
        Long citizenId = requestDTO.getCitizenId();
        return fieldHealthCareWorkerService.getHealthRecordByCitizenId(citizenId);
    }
    @PostMapping("/getFollowUpsByHealthRecordId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getFollowUpsByHealthRecordId(@RequestBody CitizenIdRequestDTO requestDTO) {
//        try {
//            List<FollowUpDTO> followUpDTOs = followUpService.getFollowUpsByHealthRecordId(requestDTO);
//            return ResponseEntity.ok(followUpDTOs);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
//        }
        Long healthRecordId = requestDTO.getCitizenId();
        return fieldHealthCareWorkerService.getFollowUpsByHealthRecordId(healthRecordId);
    }

    private static final Logger logger = LoggerFactory.getLogger(FieldHealthCareWorkerController.class);
    @GetMapping("/sms")
    public void sendMessage(@RequestParam("language") String language) {
        logger.info("Scheduled task started at {}", java.time.LocalTime.now());
        try {
            // Pass the language preference to the service
            sendSmsForFollowUp.sendMessage(language);
            logger.info("Scheduled task completed successfully at {}", java.time.LocalTime.now());
        } catch (Exception e) {
            logger.error("Scheduled task failed with error: {}", e.getMessage());
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
