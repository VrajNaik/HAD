package com.Team12.HADBackEnd.DTOs.Response;

import java.util.List;

public class ResponseDTO {
    private Long score;
    private String abhaId;
    private List<String> answers;

    // Getters and setters
    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getAbhaId() {
        return abhaId;
    }

    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}

