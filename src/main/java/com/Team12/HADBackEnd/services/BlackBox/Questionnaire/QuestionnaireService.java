package com.Team12.HADBackEnd.services.BlackBox.Questionnaire;

import com.Team12.HADBackEnd.models.Questionnaire;
import com.Team12.HADBackEnd.DTOs.Questionnaire.QuestionnaireDTO;
import com.Team12.HADBackEnd.DTOs.Questionnaire.QuestionnaireResponseDTO;

public interface QuestionnaireService {

    Questionnaire createQuestionnaire(QuestionnaireDTO questionnaireDto);

    QuestionnaireResponseDTO getQuestionnaireById(Long id);

}
