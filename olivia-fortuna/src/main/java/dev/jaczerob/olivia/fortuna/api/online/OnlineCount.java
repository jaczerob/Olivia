package dev.jaczerob.olivia.fortuna.api.online;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class OnlineCount {
    private int total;
    private int unique;
    @JsonProperty("Fortuna")
    private Map<String, Integer> channels;

    public OnlineCount() {
    }

    public OnlineCount(final int total, final int unique, final Map<String, Integer> channels) {
        this.total = total;
        this.unique = unique;
        this.channels = channels;
    }

    public int getTotal() {
        return this.total;
    }

    public int getUnique() {
        return this.unique;
    }

    public Map<String, Integer> getChannels() {
        return this.channels;
    }
}
