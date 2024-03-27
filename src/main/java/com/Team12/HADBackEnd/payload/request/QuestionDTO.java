package com.Team12.HADBackEnd.payload.request;

import java.util.List;

public class QuestionDTO {
    private String questionText;
    private List<String> options;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
