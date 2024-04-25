package com.Team12.HADBackEnd.services.BlackBox.Hospital;

import com.Team12.HADBackEnd.DTOs.Hospital.HospitalCreationDTO;
import com.Team12.HADBackEnd.DTOs.Hospital.HospitalUpdateRequestDTO;
import org.springframework.http.ResponseEntity;


public interface HospitalService {
    ResponseEntity<?> addHospital(HospitalCreationDTO hospital);
    ResponseEntity<?> getAllHospitals();
    ResponseEntity<?> updateHospital(HospitalUpdateRequestDTO request);
}
