package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenForDoctorDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpCreationByDoctorDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.HealthRecordCreationDTO;
import com.Team12.HADBackEnd.DTOs.HealthRecord.PrescriptionDTO;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.request.*;
import com.Team12.HADBackEnd.services.Doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*", exposedHeaders = "*", origins = "*")
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/getPatientsbyDocID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<List<CitizenForDoctorDTO>> getCitizensByDoctorId(@RequestParam String username) {
        List<CitizenForDoctorDTO> citizens = doctorService.getCitizensByDoctorId(username);
        return ResponseEntity.ok(citizens);
    }


    @GetMapping("/getPatientById")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<CitizenForDoctorDTO> getPatientByAbhaId(@RequestParam String abhaId) {
        CitizenForDoctorDTO citizenDTO = doctorService.getCitizenByAbhaId(abhaId);
        return ResponseEntity.ok(citizenDTO);
    }


    @PostMapping("/createHealthRecord")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> createHealthRecord(@RequestBody HealthRecordCreationDTO healthRecordCreationDTO) {
        return doctorService.createHealthRecord(healthRecordCreationDTO);
    }

    @PostMapping("/addPrescription")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> addPrescriptionToHealthRecord(@RequestBody PrescriptionDTO prescriptionDTO) {
        return doctorService.addPrescriptionToHealthRecord(prescriptionDTO);
    }

    @PostMapping("/editPrescription")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> editPrescription(@RequestBody PrescriptionDTO editPrescriptionDTO) {
        return doctorService.editLastPrescription(editPrescriptionDTO);
    }

    @PostMapping("/addFollowUp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> addFollowUp(@RequestBody FollowUpCreationByDoctorDTO followUpDTO) {
        return doctorService.addFollowUp(followUpDTO);
    }

    @PostMapping("/getFollowUp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getResponse(@RequestBody FollowUpCreationByDoctorDTO followUpDTO) {
        doctorService.addFollowUp(followUpDTO);
        String username = "";
        try {
            DoctorDTO doctorDTO = doctorService.getDoctorByUsername(username);
            if (doctorDTO == null) {
                String message = "Doctor Not Found with a given username";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
            }
            return ResponseEntity.ok(doctorDTO);
        } catch (NotFoundException ex) {
            String message = "Doctor Not Found with a given username";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getByUsername")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorByUsername(@RequestParam String username) {
        DoctorDTO doctorDTO = doctorService.getDoctorByUsername(username);
        return ResponseEntity.ok(doctorDTO);
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
//
//    @PostMapping("/addDoctor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Doctor addDoctor(@RequestBody Doctor doctor) {
//        try{
//            return doctorService.addDoctor(doctor);
//        }
//        catch (DuplicateLicenseIdException | DuplicateEmailIdException e) {
//            throw new AuthenticationServiceException(e.getMessage(), e);
//        }
//    }
//
//    @GetMapping("/viewDoctors")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<DoctorDTO>> viewDoctors() {
//        List<DoctorDTO> doctorDTOs = doctorService.getAllDoctorsWithDistricts();
//        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
//    }
//
//
//    @PutMapping("/updateDoctor")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorUpdateRequestDTO request) {
//        DoctorDTO updatedDoctorDTO = doctorService.updateDoctor(request);
//        return ResponseEntity.ok(updatedDoctorDTO);
//    }
//
//
//    @PutMapping("/deactivate")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> deactivateDoctor(@RequestBody UsernameDTO usernameDTO) {
//        try {
//            User user = userRepository.findByUsername(usernameDTO.getUsername())
//                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
//            doctorService.setActiveStatusByUsername(usernameDTO.getUsername(), false);
//            return ResponseEntity.ok().build();
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (DoctorNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (DoctorAlreadyDeactivatedException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//    @PutMapping("/activate")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> activateDoctor(@RequestBody UsernameDTO usernameDTO) {
//        try {
//            User user = userRepository.findByUsername(usernameDTO.getUsername())
//                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + usernameDTO.getUsername()));
//            doctorService.setActiveStatusByUsername(usernameDTO.getUsername(), true);
//            return ResponseEntity.ok().build();
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (DoctorNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (DoctorAlreadyDeactivatedException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }



//
//    @PostMapping("/addFollowUp")
////    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
//    public FollowUpDTO createFollowUp(@RequestBody FollowUpCreationDTO followUpDTO) {
//        return doctorService.createFollowUp(followUpDTO);
//    }



//        if (citizenDTO != null) {
//            return ResponseEntity.ok(citizenDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }

//        try {
//            DoctorDTO doctorDTO = doctorService.getDoctorByUsername(username);
//            if (doctorDTO == null) {
//                String message = "Doctor Not Found with a given username";
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
//            }
//            return ResponseEntity.ok(doctorDTO);
//        } catch (NotFoundException ex) {
//            String message = "Doctor Not Found with a given username";
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", message));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }