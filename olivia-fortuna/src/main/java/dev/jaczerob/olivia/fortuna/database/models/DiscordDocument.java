package dev.jaczerob.olivia.fortuna.database.models;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("discord")
public class DiscordDocument {
    @Id
    private String discordID;
    private Set<String> discordUsernames;
    private Set<String> fortunaCharacters;

    public String getDiscordID() {
        return this.discordID;
    }

    public void setDiscordID(final String discordID) {
        this.discordID = discordID;
    }

    public Set<String> getDiscordUsernames() {
        return this.discordUsernames;
    }

    public void setDiscordUsernames(final Set<String> discordUsernames) {
        this.discordUsernames = discordUsernames;
    }

    public Set<String> getFortunaCharacters() {
        return this.fortunaCharacters;
    }

    public void setFortunaCharacters(final Set<String> fortunaCharacters) {
        this.fortunaCharacters = fortunaCharacters;
    }

    @Override
    public String toString() {
        return "DiscordDocument[discordId=%s, discordUsernames=%s, fortunaCharacters=%s]".formatted(this.discordID, this.discordUsernames, this.fortunaCharacters);
    }
}
