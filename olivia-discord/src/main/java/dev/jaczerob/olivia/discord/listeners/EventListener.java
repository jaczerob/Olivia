package dev.jaczerob.olivia.discord.listeners;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.aop.MeterTag;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "discord.listeners.event-tracking.enabled", havingValue = "true", matchIfMissing = true)
public class EventListener extends ListenerAdapter {
    @Override
    @Counted("discord.event.count")
    public void onGenericEvent(
            final @MeterTag(value = "event_name", expression = "event.getClass().getSimpleName()") GenericEvent event
    ) {

    }
}
