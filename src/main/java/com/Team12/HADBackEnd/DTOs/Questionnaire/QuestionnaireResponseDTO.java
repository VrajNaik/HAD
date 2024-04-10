package com.Team12.HADBackEnd.DTOs.Questionnaire;

import java.util.List;

public class QuestionnaireResponseDTO {
    private Long id;
    private String name;
    private List<QuestionResponseDTO> questions;

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

    public List<QuestionResponseDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponseDTO> questions) {
        this.questions = questions;
    }
}
