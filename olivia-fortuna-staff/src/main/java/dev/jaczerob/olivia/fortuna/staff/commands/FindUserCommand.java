package dev.jaczerob.olivia.fortuna.staff.commands;

import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import dev.jaczerob.olivia.fortuna.staff.database.DiscordDocument;
import dev.jaczerob.olivia.fortuna.staff.database.DiscordRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FindUserCommand implements ICommand {
    @Autowired
    private DiscordRepository discordRepository;

    @Override
    public String name() {
        return "finduser";
    }

    @Override
    public String description() {
        return "Finds a user by their character name";
    }

    @Override
    public List<? extends OptionData> options() {
        return List.of(
                new OptionData(OptionType.STRING, "character", "the in game character name", false),
                new OptionData(OptionType.STRING, "id", "the user's discord id", false),
                new OptionData(OptionType.STRING, "username", "the user's discord name", false)
        );
    }

    @Override
    public void execute(final CommandContext context) {
        final UserOptions userOptions = context.options(UserOptions.class);

        final Optional<DiscordDocument> optionalDiscordDocument;
        final String title;
        if (userOptions.character != null) {
            optionalDiscordDocument = this.discordRepository.findByCharacterName(userOptions.character);
            title = userOptions.character;
        } else if (userOptions.id != null) {
            optionalDiscordDocument = this.discordRepository.findByDiscordID(userOptions.id);
            title = userOptions.id;
        } else if (userOptions.username != null) {
            optionalDiscordDocument = this.discordRepository.findByDiscordUsername(userOptions.username);
            title = userOptions.username;
        } else {
            context.reply("must provide one option");
            return;
        }

        if (optionalDiscordDocument.isEmpty()) {
            context.reply("not found");
            return;
        }

        final DiscordDocument discordDocument = optionalDiscordDocument.get();
        final MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle(title)
                .setDescription("Discord ID: %s".formatted(discordDocument.getId()))
                .addField("Discord Usernames", String.join("\n", discordDocument.getDiscordUsernames()), false)
                .addField("Characters", String.join("\n", discordDocument.getFortunaCharacters()), false)
                .setColor(0xFFFF00)
                .build();

        context.reply(messageEmbed);
    }

    static class UserOptions {
        public String character;
        public String id;
        public String username;
    }
}
