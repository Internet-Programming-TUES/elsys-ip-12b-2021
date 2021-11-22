package org.elsys.ip.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OurController {

    final QuestionBank bank;
    final QuestionStringBuilder stringBuilder;
    Question current;
    boolean isGameOver = false;

    public OurController(QuestionBank bank, QuestionStringBuilder stringBuilder) {
        this.bank = bank;
        this.stringBuilder = stringBuilder;
    }

    @GetMapping("/question")
    public String executeGet() {

        if (isGameOver) return "Game over. No more questions";

        current = bank.getQuestion();
        if (current == null) return "Congrats. You finished the quiz!";

        String questionString = stringBuilder.toString(current);

        return questionString;

    }

    @GetMapping("/submit/{id}")
    public String executeGet(@PathVariable String id) {

        int answerIndex = stringBuilder.getAnswerIndex(id);

        if (answerIndex >= current.getAnswers().size()) {
            answerIndex = -1;
            return "The question has " + current.getAnswers().size() + " answers.";
        }

        if (current.getAnswers().get(answerIndex).isCorrect()) {
            bank.getNextQuestion();

            return "Congrats. Onto next question";
        } else {
            isGameOver = true;
            return "Wrong! Game is over";
        }
    }
}
