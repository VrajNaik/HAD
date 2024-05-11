package com.Team12.HADBackEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String followup_status;
    private Integer age;
    private String abhaId;
    private String city;
    private boolean consent;
    private String gender;
    private String month;
}
