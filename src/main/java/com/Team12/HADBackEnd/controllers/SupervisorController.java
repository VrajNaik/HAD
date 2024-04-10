package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.AssignmentRequest;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpsDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorDTO;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DoctorAlreadyDeactivatedException;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.services.Supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/supervisor")
public class SupervisorController {

    private final SupervisorService supervisorService;

    private final UserRepository userRepository;


    @Autowired
    public SupervisorController(SupervisorService supervisorService,
                                UserRepository userRepository) {
        this.supervisorService = supervisorService;
        this.userRepository = userRepository;
    }


    @GetMapping("/getLocalAreasWithinDistrict")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<LocalAreaDTO>> getLocalAreasInDistrict(@RequestParam String username) {
        List<LocalAreaDTO> localAreas = supervisorService.getAllLocalAreasByUsername(username);
        return ResponseEntity.ok(localAreas);
    }

    @PostMapping("/assignFHWToLocalArea")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<String> assignWorkerToLocalArea(@RequestBody AssignmentRequest request) {
        String result = supervisorService.assignWorkerToLocalArea(request.getUsername(), request.getLocalAreaId());
        if (result.equals("Worker assigned successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
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

    @GetMapping("/getUnassignedFHW")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<FieldHealthCareWorkerWithHealthRecordDTO>> getUnassignedFieldHealthCareWorkers(@RequestParam String username) {
        List<FieldHealthCareWorkerWithHealthRecordDTO> unassignedWorkers = supervisorService.getUnassignedFieldHealthCareWorkerDTOs(username);
        return new ResponseEntity<>(unassignedWorkers, HttpStatus.OK);
    }


    @GetMapping("/getByUsername")
    @PreAuthorize("hasRole('ADMIN') or hasRole ('SUPERVISOR')")
    public ResponseEntity<SupervisorDTO> getSupervisorByUsername(@RequestParam String username) {
        SupervisorDTO supervisorDTO = supervisorService.getSupervisorByUsername(username);
        return ResponseEntity.ok(supervisorDTO);
    }


    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateSupervisor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            supervisorService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
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


//    @PostMapping("/addSupervisor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Supervisor addSupervisor(@RequestBody Supervisor supervisor) {
//        return supervisorService.addSupervisor(supervisor);
//    }


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


//    @GetMapping("/getByUsername")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
//    public ResponseEntity<?> getSupervisorByUsername(@RequestParam String username) {
//        try {
//            SupervisorDTO supervisorDTO = supervisorService.getSupervisorByUsername(username);
//            if (supervisorDTO == null) {
//                String message = "Supervisor Not Found with a given username";
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
//            }
//            return ResponseEntity.ok(supervisorDTO);
//        }
//        catch (RoleNotFoundException ex) {
//            String message = "Supervisor Not Found with a given username";
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
//        }
//        catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }