package dev.jaczerob.olivia.fortuna.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RankingCharacterEntity {
    @Id
    private int id;
    private String name;
    private int level;
    private int job;

    protected RankingCharacterEntity() {
    }

    public RankingCharacterEntity(final int id, final String name, final int level, final int job) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.job = job;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getJob() {
        return this.job;
    }

    public int getLevel() {
        return this.level;
    }
}
