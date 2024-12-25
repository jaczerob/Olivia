package dev.jaczerob.olivia.bot.cron.stalker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToonHQGroupMember {
    private ToonHQToon toon;

    public ToonHQGroupMember() {
    }

    public ToonHQToon getToon() {
        return this.toon;
    }

    public void setToon(ToonHQToon toon) {
        this.toon = toon;
    }
}
