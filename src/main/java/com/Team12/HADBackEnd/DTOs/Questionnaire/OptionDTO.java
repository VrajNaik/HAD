package com.Team12.HADBackEnd.DTOs.Questionnaire;

public class OptionDTO {
    private String optionText;

    private Long optionValue;

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
