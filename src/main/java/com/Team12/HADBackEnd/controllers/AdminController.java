package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.exception.DuplicateLicenseIdException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.DoctorService;
import com.Team12.HADBackEnd.security.services.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.security.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*", origins = "*")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private FieldHealthCareWorkerService fieldHealthCareWorkerService;


    @PostMapping("/addDoctor")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        try{
            return doctorService.addDoctor(doctor);
        }
        catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @GetMapping("/getDoctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorDTO>> viewDoctors() {
        List<DoctorDTO> doctorDTOs = doctorService.getAllDoctorsWithDistricts();
        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
    }


    @PutMapping("/updateDoctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorUpdateRequestDTO request) {
        DoctorDTO updatedDoctorDTO = doctorService.updateDoctor(request);
        return ResponseEntity.ok(updatedDoctorDTO);
    }


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

    @GetMapping("/getFieldHealthCareWorkers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FieldHealthcareWorkerDTO>> getAllFieldHealthCareWorker() {
        List<FieldHealthcareWorkerDTO> worker = fieldHealthCareWorkerService.getAllFieldHealthCareWorkersWithDistricts();
        return new ResponseEntity<>(worker, HttpStatus.OK);
    }

    @PutMapping("/updateFieldHealthCareWorker")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFieldHealthCareWorker(@RequestBody SupervisorUpdateRequestDTO request) {
        FieldHealthcareWorkerDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
        return ResponseEntity.ok(updatedWorkerDTO);
    }

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
    @GetMapping("/getSupervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupervisorDTO>> getAllSupervisor() {
        List<SupervisorDTO> supervisor = supervisorService.getAllSupervisorsWithDistricts();
        return new ResponseEntity<>(supervisor, HttpStatus.OK);
    }

    @PutMapping("/updateSupervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSupervisor(@RequestBody SupervisorUpdateRequestDTO request) {
        SupervisorDTO updatedSupervisorDTO = supervisorService.updateSupervisor(request);
        return ResponseEntity.ok(updatedSupervisorDTO);
    }
    @PutMapping("/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));

            String username = usernameDTO.getUsername();
            if (username.startsWith("DR")) {
                doctorService.setActiveStatusByUsername(username, true);
            } else if (username.startsWith("FHW")) {
                fieldHealthCareWorkerService.setActiveStatusByUsername(username, true);
            } else if (username.startsWith("SV")) {
                supervisorService.setActiveStatusByUsername(username, true);
            } else {
                throw new IllegalArgumentException("Invalid username prefix");
            }
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username prefix");
        }
    }
    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));

            String username = usernameDTO.getUsername();
            if (username.startsWith("DR")) {
                doctorService.setActiveStatusByUsername(username, false);
            } else if (username.startsWith("FHW")) {
                fieldHealthCareWorkerService.setActiveStatusByUsername(username, false);
            } else if (username.startsWith("SV")) {
                supervisorService.setActiveStatusByUsername(username, false);
            } else {
                throw new IllegalArgumentException("Invalid username prefix");
            }
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username prefix");
        }
    }
}

//@PutMapping("/updateDoctor")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<?> updateDoctor(@RequestBody DoctorUpdateRequestDTO request) {
//    try {
//        DoctorDTO updatedDoctorDTO = doctorService.updateDoctor(request);
//        return ResponseEntity.ok(updatedDoctorDTO);
//    }
//    catch (RoleNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }
//}

//@PutMapping("/updateFieldHealthCareWorker")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<?> updateFieldHealthCareWorker(@RequestBody SupervisorUpdateRequestDTO request) {
//    try {
//        FieldHealthcareWorkerDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
//        return ResponseEntity.ok(updatedWorkerDTO);
//    }
//    catch (RoleNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }
//}

//@PutMapping("/updateSupervisor")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<?> updateSupervisor(@RequestBody SupervisorUpdateRequestDTO request) {
//    try {
//        SupervisorDTO updatedSupervisorDTO = supervisorService.updateSupervisor(request);
//        return ResponseEntity.ok(updatedSupervisorDTO);
//    }
//    catch (RoleNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//    }
//}