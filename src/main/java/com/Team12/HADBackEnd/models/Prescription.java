package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medication;
    private String dosage; // Example: "10 mg (tablet)", "5 ml (liquid)", "50 mg (injection)"
    @Enumerated(EnumType.STRING)
    private MedicationType medicationType;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private String customFrequency;
    private String customInstructions;


    public enum MedicationType {
        TABLET,
        LIQUID,
        INJECTION,
    }

    public enum Frequency {
        NONE,
        ONCE_DAILY,
        TWICE_DAILY,
        THREE_TIMES_DAILY,
        ONCE_WEEKLY,
        AS_NEEDED,
    }

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

    public MedicationType getMedicationType() {
        return medicationType;
    }

    public void setMedicationType(MedicationType medicationType) {
        this.medicationType = medicationType;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
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