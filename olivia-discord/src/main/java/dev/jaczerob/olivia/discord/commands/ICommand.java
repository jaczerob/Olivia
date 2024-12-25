package dev.jaczerob.olivia.discord.commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface ICommand {
    String name();

    String description();

    default List<? extends OptionData> options() {
        return List.of();
    }

    default boolean ephemeral() {
        return false;
    }

    void execute(CommandContext context);
}
