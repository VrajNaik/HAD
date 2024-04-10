package com.Team12.HADBackEnd.DTOs.LocalArea;

import com.Team12.HADBackEnd.DTOs.FieldHealthCareWorker.FieldHealthCareWorkerWithHealthRecordDTO;

public class LocalAreaDTO {
    private Long id;
    private String name;
    private String pincode;
    private FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorkerWithHealthRecordDTO;

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

    public FieldHealthCareWorkerWithHealthRecordDTO getFieldHealthcareWorkerDTO() {
        return fieldHealthCareWorkerWithHealthRecordDTO;
    }

    public void setFieldHealthcareWorkerDTO(FieldHealthCareWorkerWithHealthRecordDTO fieldHealthCareWorkerWithHealthRecordDTO) {
        this.fieldHealthCareWorkerWithHealthRecordDTO = fieldHealthCareWorkerWithHealthRecordDTO;
    }
}

