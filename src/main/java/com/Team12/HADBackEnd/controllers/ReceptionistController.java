package com.Team12.HADBackEnd.controllers;


import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.AssignDoctorRequest;
import com.Team12.HADBackEnd.services.Receptionist.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/receptionist")
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    @Autowired
    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    @GetMapping("/getDoctors")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<?> getDoctorsInHospital(@RequestParam String username) {
        return receptionistService.getDoctorsInHospital(username);
    }


//    @GetMapping("/getPatientsbyDocID")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
//    public ResponseEntity<List<CitizenForDoctorDTO>> getCitizensByDoctorId(@RequestParam String username) {
//        List<CitizenForDoctorDTO> citizens = doctorService.getCitizensByDoctorId(username);
//        return ResponseEntity.ok(citizens);
//    }

    @GetMapping("/getCitizens")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<?> getCitizensAssignedToHospital(@RequestParam String username) {
        return receptionistService.getCitizensAssignedToHospital(username);
    }

    @PostMapping("/assignDoctorToCitizen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST')")
    public ResponseEntity<?> assignDoctorToCitizen(@RequestBody AssignDoctorRequest request) {
//        boolean success = fieldHealthCareWorkerService.assignDoctorToCitizen(request.getAbhaId(), request.getDoctorUsername());
//
//        if (success) {
//            return ResponseEntity.ok("Doctor assigned to Citizen successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to assign Doctor to Citizen");
//        }
        return receptionistService.assignDoctorToCitizen(request.getAbhaId(), request.getDoctorUsername());
    }
}
