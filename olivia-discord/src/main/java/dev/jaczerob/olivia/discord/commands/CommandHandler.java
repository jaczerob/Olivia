package dev.jaczerob.olivia.discord.commands;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandHandler extends ListenerAdapter {
    private static final Logger log = LogManager.getLogger();

    private final Map<String, ICommand> commandMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private final List<ICommand> commandList;
    private final Map<String, Counter> commandsUsageCounters = new ConcurrentHashMap<>();
    private final Map<String, Timer> commandsExecutionTimers = new ConcurrentHashMap<>();

    public CommandHandler(final List<ICommand> commands, final MeterRegistry meterRegistry) {
        this.commandList = commands;

        commands.forEach(command -> {
            this.commandsUsageCounters.put(command.name(), Counter.builder("olivia_command_use")
                    .tag("type", "slash")
                    .tag("name", command.name())
                    .description("The number of slash commands used")
                    .register(meterRegistry)
            );

            this.commandsExecutionTimers.put(command.name(), Timer.builder("olivia_command_time")
                    .tag("type", "slash")
                    .tag("name", command.name())
                    .description("The time it takes a slash command to process")
                    .register(meterRegistry));
        });
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
        event.deferReply().queue();

        final String commandName = event.getName();
        if (!this.commandMap.containsKey(commandName)) {
            event.reply("Invalid command passed").setEphemeral(true).queue();
            return;
        }

        ThreadContext.put("olivia.commandName", commandName);
        ThreadContext.put("olivia.commandType", "slash");

        this.commandsUsageCounters.get(commandName).increment();

        log.info("Handling command {}", commandName);

        final ICommand command = this.commandMap.get(commandName);
        final CommandContext context = new CommandContext(event);

        this.commandsExecutionTimers.get(commandName).record(() -> executeCommand(command, context));

        ThreadContext.clearMap();
    }

    private void executeCommand(final ICommand command, final CommandContext context) {
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
