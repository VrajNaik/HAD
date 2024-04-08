package com.Team12.HADBackEnd.services.Supervisor;

import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorForAdminDTO;
import com.Team12.HADBackEnd.DTOs.Supervisor.SupervisorUpdateRequestDTO;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.request.*;

import java.util.*;

public interface SupervisorService {

    Supervisor addSupervisor(Supervisor supervisor) throws DuplicateEmailIdException;

    List<SupervisorForAdminDTO> getAllSupervisorsWithDistricts();

    SupervisorDTO updateSupervisor(SupervisorUpdateRequestDTO request);

    SupervisorDTO convertToDTO(Supervisor supervisor);

    SupervisorDTO getSupervisorByUsername(String username);

    void setActiveStatusByUsername(String username, boolean active);

    String assignWorkerToLocalArea(String username, Long localAreaId);

    List<FollowUpsDTO> getFollowUpsForSupervisor(String supervisorUsername);

    List<LocalAreaDTO> getAllLocalAreasByUsername(String username);

    LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea);

    List<FieldHealthcareWorkerDTO> getUnassignedFieldHealthCareWorkerDTOs(String username);
}
