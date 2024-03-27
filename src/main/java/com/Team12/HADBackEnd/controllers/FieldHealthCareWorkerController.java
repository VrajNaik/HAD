package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Citizen;
import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.response.DoctorNotFoundException;
import com.Team12.HADBackEnd.payload.response.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.response.UserNotFoundException;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.FieldHealthCareWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
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


    @PostMapping("/addFieldHealthCareWorker")
    @PreAuthorize("hasRole('ADMIN')")
    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker fieldHealthCareWorker) {
        try{
            return fieldHealthCareWorkerService.addFieldHealthCareWorker(fieldHealthCareWorker);
        }
        catch (DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @GetMapping("/viewFieldHealthCareWorkers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getAllFieldHealthCareWorker() {
        List<FieldHealthcareWorkerDTO> worker = fieldHealthCareWorkerService.getAllFieldHealthCareWorkersWithDistricts();
        return new ResponseEntity<>(worker, HttpStatus.OK);
    }

    @PutMapping("/updateFieldHealthCareWorker")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FieldHealthcareWorkerDTO> updateDoctor(@RequestBody SupervisorUpdateRequestDTO request) {
        FieldHealthcareWorkerDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
        return ResponseEntity.ok(updatedWorkerDTO);
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
        } catch (DoctorNotFoundException e) {
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
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorAlreadyDeactivatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/getByUsername")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFieldHealthcareWorkerByUsername(@RequestBody UsernameDTO usernameRequest) {
        String username = usernameRequest.getUsername();
        try {
            FieldHealthcareWorkerDTO workerDTO = fieldHealthCareWorkerService.getFieldHealthcareWorkerByUsername(username);
            if (workerDTO == null) {
                // Handle the case where no supervisor is found with the provided username
                String message = "Field Health Care Worker Not Found with a given username";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
            }
            return ResponseEntity.ok(workerDTO);
        } catch (DoctorNotFoundException ex) {
            // Handle the case where supervisor is not found
            String message = "Field Health Care Worker Not Found with a given username";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        } catch (Exception e) {
            // Handle other exceptions here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getUnassignedFHW")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getUnassignedFieldHealthCareWorkers() {
        List<FieldHealthcareWorkerDTO> unassignedWorkers = fieldHealthCareWorkerService.getUnassignedFieldHealthCareWorkerDTOs();
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CitizenDTO> registerCitizen(@RequestBody CitizenRegistrationDTO citizen) {
        CitizenDTO registeredCitizen = fieldHealthCareWorkerService.registerCitizen(citizen);
        return new ResponseEntity<>(registeredCitizen, HttpStatus.CREATED);
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
