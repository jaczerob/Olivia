package dev.jaczerob.olivia.common.ttr.models.population;

import dev.jaczerob.olivia.common.notifications.Notification;

public class PopulationNotification extends Notification {
    private ToontownPopulation toontownPopulation;

    public ToontownPopulation getPopulationResponse() {
        return this.toontownPopulation;
    }

    public void setPopulationResponse(final ToontownPopulation toontownPopulation) {
        this.toontownPopulation = toontownPopulation;
    }

    @Override
    public String toString() {
        return "PopulationNotification[notified=%s, received=%s, toontownPopulation=%s]".formatted(this.getNotified(), this.getReceived(), this.toontownPopulation);
    }
}
