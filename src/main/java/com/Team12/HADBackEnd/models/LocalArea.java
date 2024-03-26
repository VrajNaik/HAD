package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

@Entity
public class LocalArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToOne(mappedBy = "localArea")
    private FieldHealthCareWorker fieldHealthCareWorker;

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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public FieldHealthCareWorker getFieldHealthCareWorker() {
        return fieldHealthCareWorker;
    }

    public void setFieldHealthCareWorker(FieldHealthCareWorker fieldHealthCareWorker) {
        this.fieldHealthCareWorker = fieldHealthCareWorker;
    }
}

