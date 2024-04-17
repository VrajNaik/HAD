package com.Team12.HADBackEnd.models;

import jakarta.persistence.*;

@Entity
@Table(name = "question_option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String optionText;

    private Long optionValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Long getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(Long optionValue) {
        this.optionValue = optionValue;
    }
}
