package dev.jaczerob.olivia.fortuna.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DiscordEntity {
    @Id
    private String id;
    private String discordid;
    private String name;

    protected DiscordEntity() {
    }

    public DiscordEntity(final String id, final String discordid, final String name) {
        this.id = id;
        this.discordid = discordid;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getDiscordid() {
        return this.discordid;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "DiscordEntity[id=%s, discordid=%s, name=%s]".formatted(this.id, this.discordid, this.name);
    }
}
