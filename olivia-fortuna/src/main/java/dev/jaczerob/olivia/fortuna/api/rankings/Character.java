package dev.jaczerob.olivia.fortuna.api.rankings;

public class Character {
    private int rank;
    private String name;
    private int level;
    private int jobId;

    public Character() {
    }

    public Character(final int rank, final String name, final int level, final int jobId) {
        this.rank = rank;
        this.name = name;
        this.level = level;
        this.jobId = jobId;
    }

    public int getRank() {
        return this.rank;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getJobId() {
        return this.jobId;
    }
}
