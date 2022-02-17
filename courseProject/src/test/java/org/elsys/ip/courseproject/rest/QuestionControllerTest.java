package org.elsys.ip.courseproject.rest;

import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.model.Answer;
import org.elsys.ip.courseproject.model.Question;
import org.elsys.ip.courseproject.model.QuestionRepository;
import org.elsys.ip.courseproject.service.UserService;
import org.elsys.ip.courseproject.web.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QuestionRepository repo;

    @Autowired
    private UserService userService;

    private String questionId;

    @Test
    public void getQuestion() throws Exception {
        Question question = this.restTemplate.
                withBasicAuth("admin@admin.com", "password").
                getForObject("http://localhost:" + port + "/api/question?id=" + questionId,
                Question.class);

        assertThat(question.getText()).contains("Kolko e 2+2?");
    }

    @Test
    public void noAuth() throws Exception {
        ResponseEntity<String> response = this.restTemplate.
                // NO AUTH HEADER
                getForEntity("http://localhost:" + port + "/api/question?id=2e31a8ec-7466-4259-9690-3509981e62a0",
                        String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getNotExistingQuestion() throws Exception {
        ResponseEntity<String> response = this.restTemplate.
                withBasicAuth("admin@admin.com", "password").
                getForEntity("http://localhost:" + port + "/api/question?id=2e31a8ec-7466-4259-9690-3509981e62a0",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @BeforeEach
    public void setUp() throws UserAlreadyExistException {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Admin");
        userDto.setLastName("Admin");
        userDto.setEmail("admin@admin.com");
        userDto.setPassword("password");
        userService.registerNewUserAccount(userDto);

        assertThat(repo.count()).isEqualTo(0);

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

        questionId = question.getId().toString();
    }
}
