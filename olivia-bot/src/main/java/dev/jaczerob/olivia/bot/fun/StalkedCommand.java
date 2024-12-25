package dev.jaczerob.olivia.bot.fun;

import dev.jaczerob.olivia.bot.cron.stalker.models.StalkedToon;
import dev.jaczerob.olivia.bot.services.StalkedToonsService;
import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StalkedCommand implements ICommand {
    @Autowired
    private StalkedToonsService stalkedToonsService;

    @Override
    public String name() {
        return "stalked";
    }

    @Override
    public String description() {
        return "lists all stalked toons";
    }

    @Override
    public void execute(final CommandContext context) {
        final List<StalkedToon> stalkedToons = this.stalkedToonsService.getToons();

        final MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle("Stalked Toons")
                .addField("Name", stalkedToons.stream().map(StalkedToon::name).collect(Collectors.joining("\n")), true)
                .addField("Species", stalkedToons.stream().map(StalkedToon::species).collect(Collectors.joining("\n")), true)
                .addField("Laff", stalkedToons.stream().map(toon -> String.valueOf(toon.laff())).collect(Collectors.joining("\n")), true)
                .build();

        context.reply(messageEmbed);
    }
}
