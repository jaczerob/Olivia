package dev.jaczerob.olivia.common.notifications;

import java.io.Serializable;
import java.time.Instant;

public abstract class Notification implements Serializable {
    private Instant notified = Instant.MIN;
    private Instant received = Instant.MIN;

    public Instant getNotified() {
        return this.notified;
    }

    public void setNotified(final Instant notified) {
        this.notified = notified;
    }

    public Instant getReceived() {
        return this.received;
    }

    public void setReceived(final Instant received) {
        this.received = received;
    }
}
