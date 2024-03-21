package com.Team12.HADBackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    @OneToOne(mappedBy = "district")
//    private Supervisor supervisor;
    @OneToMany(mappedBy = "district")
    private List<Doctor> doctors = new ArrayList<>();

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

//    public Supervisor getSupervisor() {
//        return supervisor;
//    }
//
//    public void setSupervisor(Supervisor supervisor) {
//        this.supervisor = supervisor;
//    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
