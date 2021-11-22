package org.elsys.ip.springweb;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String question;
    private final List<Answer> answers;

    private Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public static Builder builder() { return new Builder(); };

    public static class Builder {
        private String question;
        private List<Answer> answers = new ArrayList<>();

        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder addAnswer(String answer) {
            answers.add(new Answer(answer, false));
            return this;
        }

        public Builder addCorrectAnswer(String answer) {
            answers.add(new Answer(answer, true));
            return this;
        }

        public Question build() {
            // TODO: Check if the question has 1 correct answer
            return new Question(question, answers);
        }
    }

    public static class Answer {
        private final String answer;
        private final Boolean correct;

        public Answer(String answer, Boolean correct) {
            this.answer = answer;
            this.correct = correct;
        }

        public String getAnswer() {
            return answer;
        }

        public Boolean isCorrect() {
            return correct;
        }
    }
}
