package dev.jaczerob.olivia.discord.commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface ICommand {
    String name();

    String description();

    List<? extends OptionData> options();

    void execute(CommandContext context);
}
