package org.elsys.ip.courseproject.rest;

import org.elsys.ip.courseproject.model.Question;
import org.elsys.ip.courseproject.model.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionRepository repository;

    public QuestionController(QuestionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Question getQuestion(String id) {
        Optional<Question> question = repository.findById(UUID.fromString(id));
        if (question.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return question.get();
    }
}
