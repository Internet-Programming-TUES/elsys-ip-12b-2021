package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.QuestionNotExistException;
import org.elsys.ip.courseproject.error.RoomAlreadyExistException;
import org.elsys.ip.courseproject.error.RoomAlreadyStartedException;
import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.model.*;
import org.elsys.ip.courseproject.web.dto.AnswerDto;
import org.elsys.ip.courseproject.web.dto.QuestionDto;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public Optional<QuestionDto> getQuestionByIndex(int index) throws QuestionNotExistException {
        Optional<Question> questionByIndex = StreamSupport.stream(questionRepository.findAll().spliterator(), false).skip(index).findFirst();

        return questionByIndex.map(this::convert);
    }

    private QuestionDto convert(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId().toString());
        dto.setText(question.getText());
        dto.setAnswers(question.getAnswers().stream().map(a -> convert(a)).collect(Collectors.toList()));
        return dto;
    }

    private AnswerDto convert(Answer answer) {
        AnswerDto dto = new AnswerDto();
        dto.setId(answer.getId().toString());
        dto.setText(answer.getText());
        return dto;
    }

    private Question getQuestionEntityById(String questionId) throws QuestionNotExistException {
        Optional<Question> question = Optional.empty();
        try {
            question = questionRepository.findById(UUID.fromString(questionId));
        } catch (Exception ex) {
            //Do nothing
        }
        if (question.isEmpty()) {
            throw new QuestionNotExistException("Question with id " + questionId + " doesn't exist.");
        }

        return question.get();
    }
}
