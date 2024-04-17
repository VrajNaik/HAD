package com.Team12.HADBackEnd.DTOs.Questionnaire;

import java.util.List;

public class QuestionResponseDTO {
    private Long id;
    private String questionText;
    private List<String> optionText;
    private List<Long> optionValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptionText() {
        return optionText;
    }

    public void setOptionText(List<String> optionText) {
        this.optionText = optionText;
    }

    public List<Long> getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(List<Long> optionValue) {
        this.optionValue = optionValue;
    }
}
