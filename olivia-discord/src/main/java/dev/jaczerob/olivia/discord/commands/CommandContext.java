package dev.jaczerob.olivia.discord.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SlashCommandInteractionEvent event;
    private boolean replied = false;

    public CommandContext(final SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public SlashCommandInteractionEvent event() {
        return this.event;
    }

    public <T> T options(final Class<T> clazz) {
        final Map<String, Object> optionMap = new HashMap<>();

        this.event.getOptions().forEach(option -> {
            final String key = option.getName();
            final Object value = switch (option.getType()) {
                case STRING -> option.getAsString();
                case INTEGER -> option.getAsInt();
                case BOOLEAN -> option.getAsBoolean();
                case NUMBER -> option.getAsDouble();
                default -> null;
            };

            optionMap.put(key, value);
        });

        return MAPPER.convertValue(optionMap, clazz);
    }

    public boolean replied() {
        return this.replied;
    }

    public void reply(final String message) {
        final MessageCreateData messageCreateData = new MessageCreateBuilder()
                .addContent(message)
                .build();

        this.reply(messageCreateData);
    }

    public void reply(final MessageEmbed... embeds) {
        final MessageCreateData messageCreateData = new MessageCreateBuilder()
                .addEmbeds(embeds)
                .build();

        this.reply(messageCreateData);
    }

    public void reply(final MessageCreateData messageCreateData) {
        if (this.replied()) {
            throw new IllegalStateException("context was already replied to");
        }

        this.event.getInteraction().getHook().sendMessage(messageCreateData).queue();
        this.replied = true;
    }
}
