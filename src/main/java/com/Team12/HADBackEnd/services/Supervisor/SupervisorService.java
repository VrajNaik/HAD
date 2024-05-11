package com.Team12.HADBackEnd.services.Supervisor;

import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;
import com.Team12.HADBackEnd.DTOs.FollowUp.FollowUpsDTO;
import com.Team12.HADBackEnd.DTOs.LocalArea.LocalAreaDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import org.springframework.http.ResponseEntity;

import java.util.*;

public interface SupervisorService {

    Supervisor addSupervisor(Supervisor supervisor) throws DuplicateEmailIdException;

    List<SupervisorForAdminDTO> getAllSupervisorsWithDistricts();

    List<SupervisorForAdminDTO> getFreeActiveSupervisors();

    ResponseEntity<?> assignSupervisorToDistrict(String oldUsername, String newUsername, Long districtId);

    SupervisorDTO updateSupervisor(SupervisorUpdateRequestDTO request);

    SupervisorDTO convertToDTO(Supervisor supervisor);

    SupervisorDTO getSupervisorByUsername(String username);

    void setActiveStatusByUsername(String username, boolean active);

    String assignWorkerToLocalArea(String username, Long localAreaId);

    List<FollowUpsDTO> getFollowUpsForSupervisor(String supervisorUsername);

    List<LocalAreaDTO> getAllLocalAreasByUsername(String username);

    LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea);

    List<FieldHealthCareWorkerWithHealthRecordDTO> getUnassignedFieldHealthCareWorkerDTOs(String username);
}
