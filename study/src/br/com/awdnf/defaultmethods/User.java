package br.com.awdnf.defaultmethods;

/**
 * Created by alexandrewiggert on 26/11/15.
 */
public class User {

    private long id;
    private String name;
    private int points;
    private boolean moderator;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public User(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public User(long id, String name, int points) {
        this.id = id;
        this.name = name;
        this.points = points;
    }

    public long getId() {
        return this.id;
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

    public void toggleModerator() {
        this.moderator = !this.moderator;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.points + " - " + this.moderator;
    }

}
