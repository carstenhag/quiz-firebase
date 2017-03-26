package de.chagemann.carsten.quiz.model;

import java.util.List;

/**
 * Created by carstenh on 24.03.2017.
 */

public class Question {

    private String question;
    private List<String> answers;

    private int points;
    private String UID;

    private int timesShown = 0;
    private int timesRight = 0;
    private int timesWrong = 0;
    private int timesSkipped = 0;
    private int timesUsedJokers = 0;

    public Question() {}

    public Question(String question, List<String> answers, int points, String UID) {
        this.question = question;
        this.answers = answers;
        this.points = points;
        this.UID = UID;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getPoints() {
        return points;
    }

    public String getUID() {
        return UID;
    }

    public int getTimesShown() {
        return timesShown;
    }

    public int getTimesRight() {
        return timesRight;
    }

    public int getTimesWrong() {
        return timesWrong;
    }

    public int getTimesSkipped() {
        return timesSkipped;
    }

    public int getTimesUsedJokers() {
        return timesUsedJokers;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setTimesShown(int timesShown) {
        this.timesShown = timesShown;
    }

    public void setTimesRight(int timesRight) {
        this.timesRight = timesRight;
    }

    public void setTimesWrong(int timesWrong) {
        this.timesWrong = timesWrong;
    }

    public void setTimesSkipped(int timesSkipped) {
        this.timesSkipped = timesSkipped;
    }

    public void setTimesUsedJokers(int timesUsedJokers) {
        this.timesUsedJokers = timesUsedJokers;
    }
}
