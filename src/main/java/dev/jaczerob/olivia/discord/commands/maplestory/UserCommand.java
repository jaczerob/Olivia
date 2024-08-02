package dev.jaczerob.olivia.discord.commands.maplestory;

import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import dev.jaczerob.olivia.maplestory.CharacterData;
import dev.jaczerob.olivia.maplestory.MapleStoryGGAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
public class UserCommand implements ICommand {
    @Autowired
    private MapleStoryGGAPI mapleStoryGGAPI;

    @Override
    public String name() {
        return "maplestory_character";
    }

    @Override
    public String description() {
        return "Shows your MapleStory character";
    }

    @Override
    public List<? extends OptionData> options() {
        return List.of(
                new OptionData(OptionType.STRING, "character", "Your MapleStory character", true)
        );
    }

    @Override
    public void execute(final CommandContext context) {
        final CharacterOptions characterOptions = context.options(CharacterOptions.class);
        final CharacterData characterData = this.mapleStoryGGAPI.getCharacterData(characterOptions.character).characterData;
        if (characterData == null) {
            context.reply("Could not find your character");
            return;
        }

        final MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.PINK)
                .setTitle("Character data for %s".formatted(characterData.name))
                .setDescription("Lv. %d | %s".formatted(characterData.level, characterData.server))
                .setImage(characterData.characterImageURL)
                .build();

        context.reply(embed);
    }

    public static class CharacterOptions {
        public String character;
    }
}
