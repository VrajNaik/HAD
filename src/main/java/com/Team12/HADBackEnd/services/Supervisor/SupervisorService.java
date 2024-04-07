package com.Team12.HADBackEnd.services.Supervisor;

import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.request.*;

import jakarta.mail.MessagingException;

import java.util.*;

public interface SupervisorService {

    Supervisor addSupervisor(Supervisor supervisor) throws DuplicateEmailIdException;

    List<SupervisorDTO> getAllSupervisorsWithDistricts();

    SupervisorDTO updateSupervisor(SupervisorUpdateRequestDTO request);

    SupervisorDTO convertToDTO(Supervisor supervisor);

    SupervisorDTO getSupervisorByUsername(String username);

    void setActiveStatusByUsername(String username, boolean active);

    String generateUniqueUsername();

    String generateRandomPassword();

    void sendCredentialsByEmail(String email, String username, String password) throws MessagingException;

    String assignWorkerToLocalArea(String username, Long localAreaId);

    List<FollowUpsDTO> getFollowUpsForSupervisor(String supervisorUsername);

    List<LocalAreaDTO> getAllLocalAreasByUsername(String username);

    LocalAreaDTO convertToLocalAreaDTO(LocalArea localArea);

    List<FieldHealthcareWorkerDTO> getUnassignedFieldHealthCareWorkerDTOs(String username);
}
