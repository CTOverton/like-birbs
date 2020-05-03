package edu.psu.cto5068.like_birbs.leaderbirb;

public class HighScore {
    private String username;
    private int score;
    private String env;

    public HighScore(String username, String env, int score) {
        this.username = username;
        this.env = env;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
