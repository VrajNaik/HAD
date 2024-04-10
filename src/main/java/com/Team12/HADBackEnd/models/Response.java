package com.Team12.HADBackEnd.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long score;
    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;
    private Long followUpNo;
    private List<String> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Long getFollowUpNo() {
        return followUpNo;
    }

    public void setFollowUpNo(Long followUpNo) {
        this.followUpNo = followUpNo;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}

