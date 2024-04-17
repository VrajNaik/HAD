package com.Team12.HADBackEnd.DTOs.HealthRecord;

import java.util.List;

public class HealthRecordUpdateDTO {
    private String abhaId;
    private String prescription;
    private String conclusion;
    private String diagnosis;
    private List<Long> icd10CodeIds;


    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }


    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
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

