package com.Team12.HADBackEnd.DTOs.HealthRecord;

import com.Team12.HADBackEnd.models.Prescription;


public class PrescriptionsDTO {
    private Long id;
    private String medication;
    private String dosage; // Example: "10 mg (tablet)", "5 ml (liquid)", "50 mg (injection)"
    private Prescription.MedicationType medicationType;
    private Prescription.Frequency frequency;
    private String customFrequency;
    private String customInstructions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Prescription.MedicationType getMedicationType() {
        return medicationType;
    }

    public void setMedicationType(Prescription.MedicationType medicationType) {
        this.medicationType = medicationType;
    }

    public Prescription.Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Prescription.Frequency frequency) {
        this.frequency = frequency;
    }

    public String getCustomFrequency() {
        return customFrequency;
    }

    public void setCustomFrequency(String customFrequency) {
        this.customFrequency = customFrequency;
    }

    public String getCustomInstructions() {
        return customInstructions;
    }

    public void setCustomInstructions(String customInstructions) {
        this.customInstructions = customInstructions;
    }
}
