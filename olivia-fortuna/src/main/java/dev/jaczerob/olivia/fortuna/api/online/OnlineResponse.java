package dev.jaczerob.olivia.fortuna.api.online;

public class OnlineResponse {
    private OnlineCount onlineCount;

    public OnlineResponse() {
    }

    public OnlineResponse(final OnlineCount onlineCount) {
        this.onlineCount = onlineCount;
    }

    public OnlineCount getOnlineCount() {
        return this.onlineCount;
    }
}
