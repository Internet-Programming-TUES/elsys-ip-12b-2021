package org.elsys.ip.courseproject;

import org.elsys.ip.courseproject.model.Answer;
import org.elsys.ip.courseproject.model.Question;
import org.elsys.ip.courseproject.model.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.elsys.ip.courseproject.model.Answer.correct;
import static org.elsys.ip.courseproject.model.Answer.wrong;
import static org.elsys.ip.courseproject.model.Question.question;

@SpringBootApplication
public class CourseProjectApplication {

    @Autowired
    QuestionRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(CourseProjectApplication.class, args);
    }

    @PostConstruct
    void addQuestions() {
        repo.deleteAll();
        repo.save(question("Колко е 2+2?", wrong("1"), wrong("2"), wrong("3"), correct("4")));
        repo.save(question("Кой е най-якият клас?", wrong("12а"), correct("12б"), wrong("12в"), wrong("12г")));
        repo.save(question("Ще завърша ли по ИП?", correct("да"), wrong("не")));
    }
}
