package de.chagemann.carsten.quiz.model;

/**
 * Created by carstenh on 24.03.2017.
 */

public class Player {

    private String UID;
    private int points = 0;
    private int jokersRemaining = 0;

    private int timesPlayed = 0;
    private int timesRight = 0;
    private int timesWrong = 0;
    private int timesSkipped = 0;
    private int timesUsedJokers = 0;

    public Player() {}

    public Player(String UID) {
        this.UID = UID;
    }

    public String getUID() {

        return UID;
    }

    public int getPoints() {
        return points;
    }

    public int getJokersRemaining() {
        return jokersRemaining;
    }

    public int getTimesPlayed() {
        return timesPlayed;
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


}
