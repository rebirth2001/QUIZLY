package com.quizly.quizs.services;

import com.quizly.quizs.dtos.DtoConv;
import com.quizly.quizs.dtos.QuestionDto;
import com.quizly.quizs.models.Question;
import com.quizly.quizs.models.Quiz;
import com.quizly.quizs.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Save or update a question
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Get all questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Get question by ID
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // Delete question by ID
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public List<QuestionDto> getQuestionByQuiz(Quiz quiz) {
        List<Question> questions = questionRepository.findQuestionsByQuiz(quiz);
        List<QuestionDto> questionDtos = new ArrayList<>();
        for(Question question : questions){
            QuestionDto dto = DtoConv.questionToQuestionDto(question);
            questionDtos.add(dto);
        }
        return questionDtos;
    }
}
