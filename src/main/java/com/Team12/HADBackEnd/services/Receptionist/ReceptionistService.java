package com.Team12.HADBackEnd.services.Receptionist;

import com.Team12.HADBackEnd.DTOs.Receptionist.ReceptionistDTO;
import com.Team12.HADBackEnd.DTOs.Receptionist.ReceptionistForAdminDTO;
import com.Team12.HADBackEnd.models.Receptionist;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReceptionistService {

    ResponseEntity<?> getDoctorsInHospital(String username);

    ResponseEntity<?> getCitizensAssignedToHospital(String username);

    ResponseEntity<?> assignDoctorToCitizen(String abhaId, String doctorUsername);

    Receptionist addReceptionist(Receptionist receptionist) throws DuplicateEmailIdException;

    ResponseEntity<List<ReceptionistDTO>> getAllRecptionist();

    ReceptionistForAdminDTO convertToReceptionistForAdminDTO(Receptionist receptionist);
}
