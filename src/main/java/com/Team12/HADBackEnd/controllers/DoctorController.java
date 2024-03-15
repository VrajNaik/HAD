package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Doctor;
import com.Team12.HADBackEnd.payload.request.DoctorUpdateRequest;
import com.Team12.HADBackEnd.payload.response.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.response.DuplicateLicenseIdException;
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

    @PostMapping("/addDoctor")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor addDoctor(@RequestBody Doctor doctor) {
//        return doctorService.addDoctor(doctor);
//    }
    try{
        return doctorService.addDoctor(doctor);
    } catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
        throw new AuthenticationServiceException(e.getMessage(), e);
    }
}
    @GetMapping("/viewDoctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @PutMapping("/updateDoctor")
    public ResponseEntity<Doctor> updateDoctor(@RequestBody DoctorUpdateRequest request) {
        return doctorService.updateDoctor(request);
    }
}


