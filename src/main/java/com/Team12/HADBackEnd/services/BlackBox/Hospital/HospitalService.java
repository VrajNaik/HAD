package com.Team12.HADBackEnd.services.BlackBox.Hospital;

import com.Team12.HADBackEnd.DTOs.Hospital.HospitalCreationDTO;
import com.Team12.HADBackEnd.DTOs.Hospital.HospitalUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface HospitalService {
    ResponseEntity<?> addHospital(HospitalCreationDTO hospital);
    ResponseEntity<?> getAllHospitals();
    ResponseEntity<?> getHospitalsInDistrict(Long districtID);
    ResponseEntity<?> updateHospital(HospitalUpdateRequestDTO request);
}
