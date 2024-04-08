package com.Team12.HADBackEnd.services.BlackBox.Questionnaire;

import com.Team12.HADBackEnd.models.Option;
import com.Team12.HADBackEnd.models.Question;
import com.Team12.HADBackEnd.models.Questionnaire;
import com.Team12.HADBackEnd.payload.request.QuestionDTO;
import com.Team12.HADBackEnd.payload.request.QuestionnaireDTO;
import com.Team12.HADBackEnd.payload.response.QuestionResponseDTO;
import com.Team12.HADBackEnd.payload.response.QuestionnaireResponseDTO;
import com.Team12.HADBackEnd.repository.OptionRepository;
import com.Team12.HADBackEnd.repository.QuestionRepository;
import com.Team12.HADBackEnd.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    @Autowired
    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository,
                                    QuestionRepository questionRepository,
                                    OptionRepository optionRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public Questionnaire createQuestionnaire(QuestionnaireDTO questionnaireDto) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireDto.getName());
        questionnaire = questionnaireRepository.save(questionnaire);

        for (QuestionDTO questionDto : questionnaireDto.getQuestions()) {
            Question question = new Question();
            question.setQuestionnaire(questionnaire);
            question.setQuestionText(questionDto.getQuestionText());
            question = questionRepository.save(question);

            for (String optionText : questionDto.getOptions()) {
                Option option = new Option();
                option.setQuestion(question);
                option.setOptionText(optionText);
                optionRepository.save(option);
            }
        }

        return questionnaire;
    }

    @Override
    public QuestionnaireResponseDTO getQuestionnaireById(Long id) {
        Questionnaire questionnaire = questionnaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questionnaire not found with ID: " + id));

        List<QuestionResponseDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionnaire.getQuestions()) {
            QuestionResponseDTO questionDTO = new QuestionResponseDTO();
            questionDTO.setId(question.getId());
            questionDTO.setQuestionText(question.getQuestionText());

            List<String> options = new ArrayList<>();
            for (Option option : question.getOptions()) {
                options.add(option.getOptionText());
            }
            questionDTO.setOptions(options);

            questionDTOList.add(questionDTO);
        }

        QuestionnaireResponseDTO questionnaireDTO = new QuestionnaireResponseDTO();
        questionnaireDTO.setId(questionnaire.getId());
        questionnaireDTO.setName(questionnaire.getName());
        questionnaireDTO.setQuestions(questionDTOList);

        return questionnaireDTO;
    }
}