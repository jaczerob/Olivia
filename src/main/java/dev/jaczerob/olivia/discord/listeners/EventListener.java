package dev.jaczerob.olivia.discord.listeners;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {
    private final MeterRegistry meterRegistry;

    public EventListener(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onGenericEvent(final GenericEvent event) {
        Counter.builder("discord.event.count")
                .tag("name", event.getClass().getSimpleName())
                .register(this.meterRegistry)
                .increment();
    }
}
