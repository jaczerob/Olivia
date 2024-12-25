package dev.jaczerob.olivia.discord.commands;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.MeterTag;
import jakarta.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConditionalOnProperty(value = "discord.listeners.command-handling.enabled", havingValue = "true", matchIfMissing = true)
public class CommandHandler extends ListenerAdapter {
    private static final Logger log = LogManager.getLogger();

    private final Map<String, ICommand> commandMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private final List<ICommand> commandList;

    public CommandHandler(final List<ICommand> commands) {
        this.commandList = commands;
    }

    @Override
    public void onReady(final @Nonnull ReadyEvent event) {
        final boolean loadCommands = !this.isInitialized.get();
        if (!loadCommands) return;

        final CommandLoader commandLoader = new CommandLoader();
        try {
            this.commandMap.putAll(commandLoader.loadCommands(this.commandList, event.getJDA()));
        } catch (final Exception exc) {
            throw new RuntimeException(exc);
        }

        this.isInitialized.set(true);
    }

    @Override
    public void onSlashCommandInteraction(final @Nonnull SlashCommandInteractionEvent event) {
        final String commandName = event.getName();
        if (!this.commandMap.containsKey(commandName)) {
            event.reply("Invalid command passed").setEphemeral(true).queue();
            return;
        }

        ThreadContext.put("discord.command.name", commandName);
        ThreadContext.put("discord.command.type", "slash");

        log.info("Handling command {}", commandName);

        final ICommand command = this.commandMap.get(commandName);
        final CommandContext context = new CommandContext(event);

        event.deferReply(command.ephemeral()).queue();

        this.executeCommand(command, context);

        ThreadContext.clearMap();
    }

    @Counted(value = "discord_command_use")
    @Timed(value = "discord_command_time")
    private void executeCommand(
            @MeterTag(value = "discord_command_name", expression = "command.name()") final ICommand command,
            final CommandContext context
    ) {
        try {
            command.execute(context);
        } catch (final Exception exc) {
            log.error("Could not execute command: {}", command.name(), exc);
        } finally {
            if (!context.replied()) {
                context.reply("There was an error processing your command");
            }
        }
    }
}
