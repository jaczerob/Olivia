package dev.jaczerob.olivia.bot.fun;

import dev.jaczerob.olivia.bot.cron.stalker.models.StalkedToon;
import dev.jaczerob.olivia.bot.services.StalkedToonsService;
import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnstalkCommand implements ICommand {
    @Autowired
    private StalkedToonsService stalkedToonsService;

    @Override
    public String name() {
        return "unstalk";
    }

    @Override
    public String description() {
        return "unstalks a toon";
    }

    @Override
    public List<? extends OptionData> options() {
        return List.of(
                new OptionData(OptionType.STRING, "name", "the name of the toon", true),
                new OptionData(OptionType.STRING, "species", "the species of the toon", true),
                new OptionData(OptionType.INTEGER, "laff", "the laff of the toon", true)
        );
    }

    @Override
    public void execute(final CommandContext context) {
        final StalkOptions stalkOptions = context.options(StalkOptions.class);
        final boolean unstalked = this.stalkedToonsService.removeToon(new StalkedToon(stalkOptions.name, stalkOptions.species, stalkOptions.laff));
        context.reply("Unstalked %s: %s".formatted(stalkOptions.name, unstalked));
    }

    public static class StalkOptions {
        public String name;
        public String species;
        public int laff;
    }
}
