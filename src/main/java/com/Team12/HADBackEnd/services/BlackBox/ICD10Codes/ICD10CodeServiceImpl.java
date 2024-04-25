package com.Team12.HADBackEnd.services.BlackBox.ICD10Codes;

import com.Team12.HADBackEnd.DTOs.ICD10Code.ICD10CodesForDoctorDTO;
import com.Team12.HADBackEnd.models.ICD10Code;
import com.Team12.HADBackEnd.repository.ICD10CodeRepository;
import com.Team12.HADBackEnd.util.DTOConverter.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ICD10CodeServiceImpl implements ICD10CodeService {

    private final ICD10CodeRepository icd10CodeRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public ICD10CodeServiceImpl(ICD10CodeRepository icd10CodeRepository,
                                DTOConverter dtoConverter) {
        this.icd10CodeRepository = icd10CodeRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<ICD10Code> createICD10Codes(List<ICD10Code> icd10Codes) {
        return icd10CodeRepository.saveAll(icd10Codes);
    }

    @Override
    public ICD10Code createICD10Code(ICD10Code icd10Code) {
        return icd10CodeRepository.save(icd10Code);
    }

    @Override
    public List<ICD10CodesForDoctorDTO> getAllICD10Codes() {
        List<ICD10Code> icd10Codes = icd10CodeRepository.findAll();
        return dtoConverter.convertToICD10CodesForDoctorDTO(icd10Codes);
    }

}
