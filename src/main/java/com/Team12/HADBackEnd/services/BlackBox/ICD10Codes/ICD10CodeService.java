package com.Team12.HADBackEnd.services.BlackBox.ICD10Codes;

import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.models.ICD10Code;

import java.util.List;

public interface ICD10CodeService {

    List<ICD10Code> createICD10Codes(List<ICD10Code> icd10Codes);

    ICD10Code createICD10Code(ICD10Code icd10Code);

    List<ICD10CodesForDoctorDTO> getAllICD10Codes();
}
