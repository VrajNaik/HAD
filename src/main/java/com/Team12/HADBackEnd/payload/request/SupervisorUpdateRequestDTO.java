package com.Team12.HADBackEnd.payload.request;

public class SupervisorUpdateRequestDTO {
    private String username;
    private String name;
    private int age;
    private String gender;
    private String email;
    private Long newDistrictId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getNewDistrictId() {
        return newDistrictId;
    }

    public void setNewDistrictId(Long newDistrictId) {
        this.newDistrictId = newDistrictId;
    }
}

