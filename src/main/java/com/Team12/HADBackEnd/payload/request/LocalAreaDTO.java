package com.Team12.HADBackEnd.payload.request;

public class LocalAreaDTO {
    private Long id;
    private String name;
    private String pincode;
    private FieldHealthcareWorkerDTO fieldHealthcareWorkerDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public FieldHealthcareWorkerDTO getFieldHealthcareWorkerDTO() {
        return fieldHealthcareWorkerDTO;
    }

    public void setFieldHealthcareWorkerDTO(FieldHealthcareWorkerDTO fieldHealthcareWorkerDTO) {
        this.fieldHealthcareWorkerDTO = fieldHealthcareWorkerDTO;
    }
}

