package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.DTOs.District.DistrictForAdminDTO;
import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.models.District;
import com.Team12.HADBackEnd.models.ICD10Code;
import com.Team12.HADBackEnd.models.Questionnaire;
import com.Team12.HADBackEnd.DTOs.LocalArea.CreateLocalAreasRequest;
import com.Team12.HADBackEnd.DTOs.Questionnaire.QuestionnaireDTO;
import com.Team12.HADBackEnd.DTOs.Questionnaire.QuestionnaireResponseDTO;
import com.Team12.HADBackEnd.services.BlackBox.District.DistrictService;
import com.Team12.HADBackEnd.services.BlackBox.ICD10Codes.ICD10CodeService;
import com.Team12.HADBackEnd.services.BlackBox.LocalArea.LocalAreaService;
import com.Team12.HADBackEnd.services.BlackBox.Questionnaire.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/blackBox")
public class BlackBoxController {

    private final DistrictService districtService;
    private final LocalAreaService localAreaService;
    private final ICD10CodeService icd10CodeService;
    private final QuestionnaireService questionnaireService;

    @Autowired
    public BlackBoxController(DistrictService districtService,
                              LocalAreaService localAreaService,
                              ICD10CodeService icd10CodeService,
                              QuestionnaireService questionnaireService) {
        this.districtService = districtService;
        this.localAreaService = localAreaService;
        this.icd10CodeService = icd10CodeService;
        this.questionnaireService = questionnaireService;
    }

    @PostMapping("/createDistrict")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createDistrict(@RequestBody District district) {
        districtService.createDistrict(district);
        return ResponseEntity.ok("District created successfully");
    }

    @PostMapping("/createDistricts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createDistricts(@RequestBody List<District> districts) {
        districtService.createDistricts(districts);
        return ResponseEntity.ok("Districts created successfully");
    }

    @GetMapping("/getAllDistricts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DistrictForAdminDTO>> getAllDistricts() {
        List<DistrictForAdminDTO> districtDTOs = districtService.getAllDistricts();
        return ResponseEntity.ok(districtDTOs);
    }

    @GetMapping("/getUnallocatedDistricts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DistrictForAdminDTO>> getAllDistrictsWithoutSupervisors() {
        List<DistrictForAdminDTO> districts = districtService.getAllDistrictsWithoutSupervisors();
        return ResponseEntity.ok(districts);
    }

    @PostMapping("/createLocalAreas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createLocalAreasInDistrict(@RequestBody CreateLocalAreasRequest request) {
        String message = localAreaService.createLocalAreasInDistrict(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/createICD10Codes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<ICD10Code>> createICD10Codes(@RequestBody List<ICD10Code> icd10Codes) {
        List<ICD10Code> savedICD10Codes = icd10CodeService.createICD10Codes(icd10Codes);
        return new ResponseEntity<>(savedICD10Codes, HttpStatus.CREATED);
    }


    @PostMapping("/createICD10Code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<ICD10Code> createICD10Code(@RequestBody ICD10Code icd10Code) {
        ICD10Code savedICD10Code = icd10CodeService.createICD10Code(icd10Code);
        return new ResponseEntity<>(savedICD10Code, HttpStatus.CREATED);
    }

    @GetMapping("/getICD10Codes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<List<ICD10CodesForDoctorDTO>> getAllICD10Codes() {
        List<ICD10CodesForDoctorDTO> icd10CodesDTOs = icd10CodeService.getAllICD10Codes();
        return ResponseEntity.ok(icd10CodesDTOs);
    }

    @PostMapping("/createQuestionnaire")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody QuestionnaireDTO questionnaireDto) {
        Questionnaire createdQuestionnaire = questionnaireService.createQuestionnaire(questionnaireDto);
        return ResponseEntity.ok(createdQuestionnaire);
    }

    @GetMapping("/getQuestionnaire")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getQuestionnaireById(@RequestParam Long id) {
        QuestionnaireResponseDTO questionnaireResponse = questionnaireService.getQuestionnaireById(id);
        return ResponseEntity.ok(questionnaireResponse);
    }

    @GetMapping("/getQuestionnaires")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('FIELD_HEALTHCARE_WORKER')")
    public ResponseEntity<?> getAllQuestionnaires() {
        List<QuestionnaireResponseDTO> questionnaireResponse = questionnaireService.getAllQuestionnaire();
        return ResponseEntity.ok(questionnaireResponse);
    }
}
