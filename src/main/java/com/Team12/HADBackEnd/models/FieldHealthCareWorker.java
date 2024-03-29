package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class FieldHealthCareWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String email;
    private Long phoneNum;
    private String username;
    private String password;
    private boolean active = true;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToOne
    @JoinColumn(name = "localarea_id")
    private LocalArea localArea;

    @OneToMany(mappedBy = "fieldHealthCareWorker")
    private List<Citizen> citizens;

    @OneToMany(mappedBy = "fieldHealthCareWorker")
    private List<HealthRecord> healthRecords;

    @OneToMany(mappedBy = "fieldHealthCareWorker")
    private List<FollowUp> followUps;


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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public LocalArea getLocalArea() {
        return localArea;
    }

    public void setLocalArea(LocalArea localArea) {
        this.localArea = localArea;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    public List<HealthRecord> getHealthRecords() {
        return healthRecords;
    }

    public void setHealthRecords(List<HealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    public List<FollowUp> getFollowUps() {
        return followUps;
    }

    public void setFollowUps(List<FollowUp> followUps) {
        this.followUps = followUps;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

}
