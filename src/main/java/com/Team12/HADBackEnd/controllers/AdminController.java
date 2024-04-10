package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Doctor.DoctorUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerForAdminDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerUpdateRequestDTO;
import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.exception.DuplicateLicenseIdException;
import com.Team12.HADBackEnd.payload.exception.UserNotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.repository.*;
import com.Team12.HADBackEnd.services.Doctor.DoctorService;
import com.Team12.HADBackEnd.services.FieldHealthCareWorker.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.services.Supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*", origins = "*")
@RequestMapping("/admin")
public class AdminController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final SupervisorRepository supervisorRepository;
    private final FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository;
    private final CitizenRepository citizenRepository;
    private final UserRepository userRepository;
    private final SupervisorService supervisorService;
    private final FieldHealthCareWorkerService fieldHealthCareWorkerService;

    @Autowired
    public AdminController(DoctorService doctorService,
                           DoctorRepository doctorRepository,
                           SupervisorRepository supervisorRepository,
                           FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                           CitizenRepository citizenRepository,
                           UserRepository userRepository,
                           SupervisorService supervisorService,
                           FieldHealthCareWorkerService fieldHealthCareWorkerService) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.supervisorRepository = supervisorRepository;
        this.fieldHealthCareWorkerRepository = fieldHealthCareWorkerRepository;
        this.citizenRepository = citizenRepository;
        this.userRepository = userRepository;
        this.supervisorService = supervisorService;
        this.fieldHealthCareWorkerService = fieldHealthCareWorkerService;
    }


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
    public ResponseEntity<List<DoctorForAdminDTO>> viewDoctors() {
        List<DoctorForAdminDTO> doctorDTOs = doctorService.getAllDoctorsWithDistricts();
        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
    }


    @PostMapping("/updateDoctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorUpdateRequestDTO request) {
        try {
            DoctorDTO updatedDoctorDTO = doctorService.updateDoctor(request);
            return ResponseEntity.ok(updatedDoctorDTO);
        }
        catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
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
    public ResponseEntity<List<FieldHealthCareWorkerForAdminDTO>> getAllFieldHealthCareWorker() {
        List<FieldHealthCareWorkerForAdminDTO> worker = fieldHealthCareWorkerService.getAllFieldHealthCareWorkersWithDistricts();
        return new ResponseEntity<>(worker, HttpStatus.OK);
    }


    @PostMapping("/updateFieldHealthCareWorker")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFieldHealthCareWorker(@RequestBody FieldHealthCareWorkerUpdateRequestDTO request) {
        try {
            FieldHealthCareWorkerWithHealthRecordDTO updatedWorkerDTO = fieldHealthCareWorkerService.updateFieldHealthCareWorker(request);
            return ResponseEntity.ok(updatedWorkerDTO);
        }
        catch (DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
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
    public ResponseEntity<List<SupervisorForAdminDTO>> getAllSupervisor() {
        List<SupervisorForAdminDTO> supervisor = supervisorService.getAllSupervisorsWithDistricts();
        return new ResponseEntity<>(supervisor, HttpStatus.OK);
    }


    @PostMapping("/updateSupervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSupervisor(@RequestBody SupervisorUpdateRequestDTO request) {
        try {
            SupervisorDTO updatedSupervisorDTO = supervisorService.updateSupervisor(request);
            return ResponseEntity.ok(updatedSupervisorDTO);
        }
        catch (DuplicateEmailIdException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }


    @GetMapping("/viewCitizens")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CitizenForAdminDTO> getAllCitizens() {
        return fieldHealthCareWorkerService.getAllCitizens();
    }


    @GetMapping("/getRoleCounts")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getRoleCounts() {
        long doctorCount = doctorRepository.countByActiveTrue();
        long supervisorCount = supervisorRepository.countByActiveTrue();
        long fieldWorkerCount = fieldHealthCareWorkerRepository.countByActiveTrue();
        long citizen = citizenRepository.count();
        Map<String, Object> response = new HashMap<>();
        Map<String, Long> counts = new HashMap<>();
        counts.put("doctors", doctorCount);
        counts.put("supervisors", supervisorCount);
        counts.put("fieldHealthcareWorkers", fieldWorkerCount);
        counts.put("citizens", citizen);
        response.put("counts", counts);
        return response;
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