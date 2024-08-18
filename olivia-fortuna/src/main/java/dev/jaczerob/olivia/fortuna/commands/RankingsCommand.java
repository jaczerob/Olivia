package dev.jaczerob.olivia.fortuna.commands;

import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import dev.jaczerob.olivia.fortuna.constants.JobIDs;
import dev.jaczerob.olivia.fortuna.database.models.CharacterEntity;
import dev.jaczerob.olivia.fortuna.database.repositories.FortunaRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RankingsCommand implements ICommand {
    private static final Logger log = LogManager.getLogger();

    private static final int AMOUNT_PLAYERS = 5;

    private final FortunaRepository fortunaRepository;

    public RankingsCommand(final FortunaRepository fortunaRepository) {
        this.fortunaRepository = fortunaRepository;
    }

    @Override
    public String name() {
        return "rankings";
    }

    @Override
    public String description() {
        return "Returns the top 5 players in Fortuna";
    }

    @Override
    public List<? extends OptionData> options() {
        return List.of();
    }

    @Override
    public void execute(final CommandContext context) {
        final List<CharacterEntity> rankingsResponse = this.fortunaRepository.getTopPlayers(AMOUNT_PLAYERS);
        if (rankingsResponse == null || rankingsResponse.isEmpty()) {
            log.error("Failed to get ranking data: {}", rankingsResponse);
            context.reply("Could not get rankings");
            return;
        }

        final String rankColumn = IntStream.range(1, Math.min(AMOUNT_PLAYERS, rankingsResponse.size()) + 1).mapToObj(String::valueOf).collect(Collectors.joining("\n"));
        final String levelColumn = rankingsResponse.stream().map(character -> "Lv. %d %s".formatted(character.getLevel(), JobIDs.JOB_ID_TO_JOB.getOrDefault(character.getJob(), "?"))).collect(Collectors.joining("\n"));
        final String nameColumn = rankingsResponse.stream().map(character -> String.valueOf(character.getName())).collect(Collectors.joining("\n"));

        final MessageEmbed messageEmbed = new EmbedBuilder()
                .setColor(0xFFFF00)
                .setTitle("Top 5 Players")
                .addField("Rank", rankColumn, true)
                .addField("Level", levelColumn, true)
                .addField("Name", nameColumn, true)
                .build();

        context.reply(messageEmbed);
    }
}
