package edu.psu.cto5068.like_birbs.leaderbirb;

public class HighScore {
    private int rank;
    private String username;
    private int score;
    private String env;

    private boolean highlight;

    public HighScore(int rank, String username, String env, int score, boolean highlight) {
        this.rank = rank;
        this.username = username;
        this.env = env;
        this.score = score;
        this.highlight = highlight;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
