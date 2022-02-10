package org.elsys.ip.courseproject;

import org.elsys.ip.courseproject.model.Answer;
import org.elsys.ip.courseproject.model.Question;
import org.elsys.ip.courseproject.model.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class CourseProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseProjectApplication.class, args);
    }

    @Autowired
    private QuestionRepository repo;

    @PostConstruct
    public void test() {
        System.out.println("======================");
        System.out.println(repo.findAll().iterator().hasNext());

        Question question = new Question();
        question.setText("Kolko e 2+2?");
        Answer answer1 = new Answer();
        answer1.setText("1");
        Answer answer2 = new Answer();
        answer2.setText("2");
        Answer answer3 = new Answer();
        answer3.setText("3");
        Answer answer4 = new Answer();
        answer4.setText("4");
        answer4.setCorrect(true);
        question.setAnswers(List.of(answer1, answer2, answer3, answer4));

        repo.save(question);

        Question fromDB = repo.findAll().iterator().next();
        System.out.println(fromDB.toString());
        System.out.println(repo.findAll().iterator().hasNext());
        System.out.println("======================");
    }
}
