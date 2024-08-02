package dev.jaczerob.olivia.discord.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandLoader {
    private static final Logger log = LogManager.getLogger();

    private static final Path OLIVIA_DATA_PATH = Path.of("olivia-data", "olivia-commands.json");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        if (!OLIVIA_DATA_PATH.getParent().toFile().exists()) {
            try {
                Files.createDirectory(OLIVIA_DATA_PATH.getParent());
            } catch (final IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }

    private static void writeNewCommands(final Map<String, String> commands) throws Exception {
        OBJECT_MAPPER.writeValue(OLIVIA_DATA_PATH.toFile(), commands);
    }

    private static Map<String, String> getExistingCommands() throws Exception {
        if (!OLIVIA_DATA_PATH.toFile().exists()) {
            return Map.of();
        } else {
            return OBJECT_MAPPER.readValue(OLIVIA_DATA_PATH.toFile(), new TypeReference<>() {
            });
        }
    }

    private static String commandHashCode(final SlashCommandData slashCommandData) {
        final String source = slashCommandData.getOptions().stream()
                .map(opt -> "%s_%s_%s_%s".formatted(opt.isRequired(), opt.getName(), opt.getDescription(), formatChoices(opt.getChoices())))
                .collect(Collectors.joining("_"));

        return String.valueOf(source.hashCode());
    }

    private static String formatChoices(final List<Command.Choice> choices) {
        return choices.stream()
                .map(choice -> "%s_%s".formatted(choice.getName(), choice.getType().name()))
                .collect(Collectors.joining("_"));
    }

    public Map<String, ICommand> loadCommands(final List<ICommand> commands, final JDA jda) throws Exception {
        log.info("Loading commands");

        final Map<String, String> existingCommands = getExistingCommands();

        final List<SlashCommandData> slashCommands = new ArrayList<>();
        final List<SlashCommandData> outdatedCommands = new ArrayList<>();
        final Map<String, String> currentCommandsHash = new HashMap<>();
        final Map<String, ICommand> currentCommands = new HashMap<>();

        commands.forEach(command -> {
            final SlashCommandData slashCommandData = Commands.slash(command.name(), command.description())
                    .addOptions(command.options());

            slashCommands.add(slashCommandData);
            log.info("Loading command: {}", command.name());

            final String commandHashCode = commandHashCode(slashCommandData);
            currentCommandsHash.put(command.name(), commandHashCode);
            currentCommands.put(command.name(), command);

            if (existingCommands.isEmpty() || !existingCommands.containsKey(command.name())) {
                log.info("Found new command: {}", command.name());
                outdatedCommands.add(slashCommandData);
            } else if (!existingCommands.get(command.name()).equals(commandHashCode)) {
                log.info("Found outdated command: {}", command.name());
                outdatedCommands.add(slashCommandData);
            }
        });

        if (!outdatedCommands.isEmpty()) {
            jda.updateCommands().addCommands(outdatedCommands).queue();
            writeNewCommands(currentCommandsHash);
            log.info("Updated {} commands", outdatedCommands.size());
        }

        log.info("Loaded {} commands", slashCommands.size());
        return currentCommands;
    }
}
