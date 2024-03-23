package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.request.DoctorDTO;
import com.Team12.HADBackEnd.payload.request.DoctorUpdateRequestDTO;
import com.Team12.HADBackEnd.payload.request.UsernameDTO;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.UserRepository;
import com.Team12.HADBackEnd.security.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*", origins = "*")
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/viewDoctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorDTO>> viewDoctors() {
        List<DoctorDTO> doctorDTOs = doctorService.getAllDoctorsWithDistricts();
        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
    }


    @PutMapping("/updateDoctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorUpdateRequestDTO request) {
        DoctorDTO updatedDoctorDTO = doctorService.updateDoctor(request);
        return ResponseEntity.ok(updatedDoctorDTO);
    }


    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateDoctor(@RequestBody UsernameDTO usernameDTO) {
        try {
            User user = userRepository.findByUsername(usernameDTO.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
            doctorService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
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
            doctorService.setActiveStatusByUsername(usernameDTO.getUsername(), true);
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

//    @GetMapping("/viewDoctors")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<Doctor>> getAllDoctors() {
//        List<Doctor> doctors = doctorService.getAllDoctors();
//        return new ResponseEntity<>(doctors, HttpStatus.OK);
//    }

//    @PutMapping("/updateDoctor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Doctor> updateDoctor(@RequestBody DoctorUpdateRequest request) {
//        return doctorService.updateDoctor(request);
//    }

//    @PutMapping("/activate")
//    public ResponseEntity<?> activateDoctor(@RequestBody UsernameDto usernameDto) {
//        try {
//            doctorService.setActiveStatusByUsername(usernameDto.getUsername(), true);
//            return ResponseEntity.ok().build();
//        } catch (DoctorNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (DoctorAlreadyDeactivatedException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//public ResponseEntity<List<Doctor>> getAllDoctorsWithDistricts() {
//    List<Doctor> doctors = doctorService.getAll();
//    return new ResponseEntity<>(doctors, HttpStatus.OK);
//}

