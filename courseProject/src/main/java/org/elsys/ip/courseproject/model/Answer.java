package org.elsys.ip.courseproject.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Answer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String text;

    private boolean correct;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correct=" + correct +
                '}';
    }

    public static Answer wrong(
            String text
    ) {
        Answer answer = new Answer();
        answer.setText(text);
        answer.setCorrect(false);
        return answer;
    }

    public static Answer correct(
            String text
    ) {
        Answer answer = new Answer();
        answer.setText(text);
        answer.setCorrect(true);
        return answer;
    }
}
