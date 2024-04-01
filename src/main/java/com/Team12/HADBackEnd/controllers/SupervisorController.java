package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.ICD10Code;
import com.Team12.HADBackEnd.models.Questionnaire;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.RoleNotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.request.QuestionnaireDTO;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private UserRepository userRepository;

//    @PostMapping("/addSupervisor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Supervisor addSupervisor(@RequestBody Supervisor supervisor) {
//        try{
//            return supervisorService.addSupervisor(supervisor);
//        }
//        catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
//            throw new AuthenticationServiceException(e.getMessage(), e);
//        }
//    }
//    @GetMapping("/viewSupervisors")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<SupervisorDTO>> getAllSupervisor() {
//        List<SupervisorDTO> supervisor = supervisorService.getAllSupervisorsWithDistricts();
//        return new ResponseEntity<>(supervisor, HttpStatus.OK);
//    }
//
//    @PutMapping("/updateSupervisor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<SupervisorDTO> updateDoctor(@RequestBody SupervisorUpdateRequestDTO request) {
//        SupervisorDTO updatedSupervisorDTO = supervisorService.updateSupervisor(request);
//        return ResponseEntity.ok(updatedSupervisorDTO);
//    }

    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateSupervisor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            supervisorService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
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
    public ResponseEntity<?> activateSupervisor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            supervisorService.setActiveStatusByUsername(usernameDTO.getUsername(), true);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorAlreadyDeactivatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/assignWorkerToLocalArea")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<String> assignWorkerToLocalArea(@RequestBody AssignmentRequest request) {
        String result = supervisorService.assignWorkerToLocalArea(request.getUsername(), request.getLocalAreaId());
        if (result.equals("Worker assigned successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
    @PostMapping("/getByUsername")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> getSupervisorByUsername(@RequestBody UsernameDTO usernameRequest) {
        String username = usernameRequest.getUsername();
        try {
            SupervisorDTO supervisorDTO = supervisorService.getSupervisorByUsername(username);
            if (supervisorDTO == null) {
                // Handle the case where no supervisor is found with the provided username
                String message = "Supervisor Not Found with a given username";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
            }
            return ResponseEntity.ok(supervisorDTO);
        } catch (RoleNotFoundException ex) {
            // Handle the case where supervisor is not found
            String message = "Supervisor Not Found with a given username";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        } catch (Exception e) {
            // Handle other exceptions here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/icd10codes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<ICD10Code>> createICD10Codes(@RequestBody List<ICD10Code> icd10Codes) {
        List<ICD10Code> savedICD10Codes = supervisorService.createICD10Codes(icd10Codes);
        return new ResponseEntity<>(savedICD10Codes, HttpStatus.CREATED);
    }
    @PostMapping("/icd10code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<ICD10Code> createICD10Code(@RequestBody ICD10Code icd10Code) {
        ICD10Code savedICD10Code = supervisorService.createICD10Code(icd10Code);
        return new ResponseEntity<>(savedICD10Code, HttpStatus.CREATED);
    }

    @PostMapping("/createQuestionnaire")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody QuestionnaireDTO questionnaireDto) {
        Questionnaire createdQuestionnaire = supervisorService.createQuestionnaire(questionnaireDto);
        return ResponseEntity.ok(createdQuestionnaire);
    }

    @PostMapping("/getQuestionnaire")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> getQuestionnaireById(@RequestBody QuestionnaireIdDTO questionnaireIdRequest) {
        Long id = questionnaireIdRequest.getId();
        QuestionnaireResponseDTO questionnaireResponse = supervisorService.getQuestionnaireById(id);
        return ResponseEntity.ok(questionnaireResponse);
    }

    @GetMapping("/getPendingFollowUps")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FollowUpsDTO>> getFollowUpsForSupervisor(@RequestParam String username) {
        List<FollowUpsDTO> followUps = supervisorService.getFollowUpsForSupervisor(username);
        if (followUps.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(followUps);
    }
}
//@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RequestMapping("/FieldHealthCareWorker")
//public class FieldHealthCareWorkerController {


//    @PostMapping("/addSupervisor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Supervisor addSupervisor(@RequestBody Supervisor supervisor) {
//        return supervisorService.addSupervisor(supervisor);
//    }


