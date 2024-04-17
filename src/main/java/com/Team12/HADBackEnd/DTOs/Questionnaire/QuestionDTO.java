package com.Team12.HADBackEnd.DTOs.Questionnaire;

import java.util.List;

public class QuestionDTO {
    private String questionText;
    private List<OptionDTO> options;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }
}
