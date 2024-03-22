package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addSupervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public Supervisor addSupervisor(@RequestBody Supervisor supervisor) {
        try{
            return supervisorService.addSupervisor(supervisor);
        }
        catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
    @GetMapping("/viewSupervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupervisorDTO>> getAllSupervisor() {
        List<SupervisorDTO> supervisor = supervisorService.getAllSupervisorsWithDistricts();
        return new ResponseEntity<>(supervisor, HttpStatus.OK);
    }

    @PutMapping("/updateSupervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupervisorDTO> updateDoctor(@RequestBody SupervisorUpdateRequestDTO request) {
        SupervisorDTO updatedSupervisorDTO = supervisorService.updateSupervisor(request);
        return ResponseEntity.ok(updatedSupervisorDTO);
    }

    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateDoctor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            supervisorService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
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
    public ResponseEntity<?> activateDoctor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            supervisorService.setActiveStatusByUsername(usernameDTO.getUsername(), true);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DoctorNotFoundException e) {
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


