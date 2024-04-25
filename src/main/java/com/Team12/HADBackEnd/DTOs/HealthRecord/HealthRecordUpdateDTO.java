package com.Team12.HADBackEnd.DTOs.HealthRecord;

import com.Team12.HADBackEnd.models.Prescription;

import java.util.List;

public class HealthRecordUpdateDTO {
    private String abhaId;
    private List<PrescriptionForHealthRecordDTO> prescription;
    private String conclusion;
    private String diagnosis;
    private List<Long> icd10CodeIds;


    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }


    public List<PrescriptionForHealthRecordDTO> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<PrescriptionForHealthRecordDTO> prescription) {
        this.prescription = prescription;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Long> getIcd10CodeIds() {
        return icd10CodeIds;
    }

    public void setIcd10CodeIds(List<Long> icd10CodeIds) {
        this.icd10CodeIds = icd10CodeIds;
    }
}

