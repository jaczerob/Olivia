package dev.jaczerob.olivia.fortuna.staff.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("discord")
public class DiscordDocument {
    @Id
    private String id;
    private Set<String> discordUsernames;
    private Set<String> fortunaCharacters;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
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
        return "DiscordDocument[discordId=%s, discordUsernames=%s, fortunaCharacters=%s]".formatted(this.id, this.discordUsernames, this.fortunaCharacters);
    }
}
