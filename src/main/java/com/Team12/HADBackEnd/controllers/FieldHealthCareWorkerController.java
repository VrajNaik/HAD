package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenRegistrationDTO;
import com.Team12.HADBackEnd.models.FollowUp;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;
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


    private final FieldHealthCareWorkerService fieldHealthCareWorkerService;

    private final UserRepository userRepository;

    @Autowired
    public FieldHealthCareWorkerController(FieldHealthCareWorkerService fieldHealthCareWorkerService,
                                           UserRepository userRepository) {
        this.fieldHealthCareWorkerService = fieldHealthCareWorkerService;
        this.userRepository = userRepository;
    }


    @PostMapping("/registerCitizen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<CitizenDTO> registerCitizen(@RequestBody CitizenRegistrationDTO citizen) {
        CitizenDTO registeredCitizen = fieldHealthCareWorkerService.registerCitizen(citizen);
        return new ResponseEntity<>(registeredCitizen, HttpStatus.CREATED);
    }


    @PostMapping("/getAssessmentScore")
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


    @PostMapping("/getFHWByDistrictId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getFieldHealthCareWorkersInDistrict(@RequestBody DistrictIdDTO districtRequestDTO) {
        Long districtId = districtRequestDTO.getDistrictId();
        List<FieldHealthcareWorkerDTO> unassignedWorkers = fieldHealthCareWorkerService.getFieldHealthCareWorkerDTOs(districtId);
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }


    @GetMapping("/getFollowUpsForToday")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public List<FollowUpReturnDTO> getFollowUpsForToday(@RequestParam String username) {
        return fieldHealthCareWorkerService.getFollowUpsForToday(username);
    }


    @PostMapping("/getHealthRecordById")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getHealthRecordByCitizenId(@RequestBody CitizenIdRequestDTO requestDTO) {
        Long citizenId = requestDTO.getCitizenId();
        return fieldHealthCareWorkerService.getHealthRecordByCitizenId(citizenId);
    }


    @PostMapping("/getFollowUpsByHealthRecordId")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getFollowUpsByHealthRecordId(@RequestBody CitizenIdRequestDTO requestDTO) {
        Long healthRecordId = requestDTO.getCitizenId();
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getFieldHealthcareWorkerByUsername(@RequestParam String username) {
        FieldHealthcareWorkerDTO fhwDTO = fieldHealthCareWorkerService.getFieldHealthcareWorkerByUsername(username);
        return ResponseEntity.ok(fhwDTO);
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
        }  catch (DoctorAlreadyDeactivatedException e) {
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
