package com.Team12.HADBackEnd.services.BlackBox.Hospital;

import com.Team12.HADBackEnd.DTOs.Hospital.HospitalCreationDTO;
import com.Team12.HADBackEnd.DTOs.Hospital.HospitalDTO;
import com.Team12.HADBackEnd.DTOs.Hospital.HospitalUpdateRequestDTO;
import com.Team12.HADBackEnd.models.Hospital;
import com.Team12.HADBackEnd.models.Receptionist;
import com.Team12.HADBackEnd.payload.exception.DuplicateEmailIdException;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.response.ResponseMessage;
import com.Team12.HADBackEnd.repository.HospitalRepository;
import com.Team12.HADBackEnd.repository.ReceptionistRepository;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ReceptionistRepository receptionistRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public HospitalServiceImpl(HospitalRepository hospitalRepository,
                               ReceptionistRepository receptionistRepository,
                               DTOConverter dtoConverter) {
        this.hospitalRepository = hospitalRepository;
        this.receptionistRepository = receptionistRepository;
        this.dtoConverter = dtoConverter;
    }


    @Override
    public ResponseEntity<?> addHospital(HospitalCreationDTO hospitalDTO) {

        if (hospitalRepository.existsByEmail(hospitalDTO.getEmail())) {
            throw new DuplicateEmailIdException("Hospital with the same Email ID already exists.");
        }

        if (hospitalRepository.existsByPhoneNumber(hospitalDTO.getPhoneNumber())) {
            throw new DuplicateEmailIdException("Hospital with the same Phone Number already exists.");
        }

        if (hospitalRepository.existsByUHID(hospitalDTO.getUHID())) {
            throw new DuplicateEmailIdException("Hospital with the same UHID already exists.");
        }

        Hospital hospital = new Hospital();
        if (hospitalDTO.getAddress() != null) {
            hospital.setAddress(hospitalDTO.getAddress());
        }
        if (hospitalDTO.getUHID() != null) {
            hospital.setUHID(hospitalDTO.getUHID());
        }
        if (hospitalDTO.getName() != null) {
            hospital.setName(hospitalDTO.getName());
        }
        if (hospitalDTO.getPhoneNumber() != null) {
            hospital.setPhoneNumber(hospitalDTO.getPhoneNumber());
        }
        if (hospitalDTO.getEmail() != null) {
            hospital.setEmail(hospitalDTO.getEmail());
        }
        if (hospitalDTO.getOwnershipType() != null){
            hospital.setOwnershipType(hospitalDTO.getOwnershipType());
        }

        hospital.setNumberOfBeds(hospitalDTO.getNumberOfBeds());

        Receptionist receptionist = receptionistRepository.findById(hospitalDTO.getReceptionistId())
                .orElseThrow(() -> new NotFoundException("Receptionist not found"));

        hospital.setReceptionist(receptionist);

        hospitalRepository.save(hospital);

        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Hospital Add Successfully!");
    }

    @Override
    public ResponseEntity<?> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<HospitalDTO> hospitalDTOs = hospitals.stream()
                .map(dtoConverter::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hospitalDTOs);
    }


    @Override
    public ResponseEntity<?> updateHospital(HospitalUpdateRequestDTO request) {
        Hospital hospital = hospitalRepository.findByUHID(request.getUHID())
                .orElseThrow(() -> new NotFoundException("Hospital Not Found with this UHID: " + request.getUHID()));

        if (hospitalRepository.existsByEmail(request.getEmail()) && !Objects.equals(hospital.getEmail(), request.getEmail())) {
            throw new DuplicateEmailIdException("Hospital with the same Email ID already exists.");
        }

        if (hospitalRepository.existsByPhoneNumber(request.getPhoneNumber()) && !Objects.equals(hospital.getPhoneNumber(), request.getPhoneNumber())) {
            throw new DuplicateEmailIdException("Hospital with the same Phone Number already exists.");
        }
        if(request.getAddress() != null){
            hospital.setAddress(request.getAddress());
        }
        if(request.getName() != null){
            hospital.setName(request.getName());
        }
        if(request.getPhoneNumber() != null){
            hospital.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getEmail() != null){
            hospital.setEmail(request.getEmail());
        }
        if(request.getOwnershipType() != null){
            hospital.setOwnershipType(request.getOwnershipType());
        }
        hospital.setNumberOfBeds(request.getNumberOfBeds());


        if (request.getReceptionistUsername() != null && !request.getReceptionistUsername().isEmpty()) {
            Receptionist receptionist = receptionistRepository.findByUsername(request.getReceptionistUsername())
                    .orElseThrow(() -> new NotFoundException("Receptionist not found with provided username: " + request.getReceptionistUsername()));
            if (receptionist != null) {
                hospital.setReceptionist(receptionist);
            }
        }

        hospitalRepository.save(hospital);
        return ResponseMessage.createSuccessResponse(HttpStatus.OK, "Hospital Updated Successfully!");
    }

}