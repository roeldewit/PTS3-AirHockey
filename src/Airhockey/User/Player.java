package Airhockey.User;

import Airhockey.Elements.Bat;

/**
 *
 * @author Roel
 */
public class Player {

    private int id;
    private int score = 20;
    private Bat bat;
    public User user;

    public Player(int id, User user) {
        this.id = id;
        this.user = user;
    }

    public void upScore() {
        score++;
    }

    public void downScore() {
        score--;
    }

    public void setBat(Bat bat) {
        this.bat = bat;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bat getBat() {
        return bat;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
