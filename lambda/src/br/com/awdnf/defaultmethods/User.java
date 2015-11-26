package br.com.awdnf.defaultmethods;

/**
 * Created by alexandrewiggert on 26/11/15.
 */
public class User {

    private String name;
    private int points;
    private boolean moderator;

    public User(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void becomeModerator() {
        this.moderator = true;
    }

    public boolean isModerator() {
        return moderator;
    }

    public String getDisplayName() {
        return this.name + " - " + this.points;
    }

}
