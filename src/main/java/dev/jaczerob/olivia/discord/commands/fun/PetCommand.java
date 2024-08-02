package dev.jaczerob.olivia.discord.commands.fun;

import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetCommand implements ICommand {
    @Override
    public String name() {
        return "pet";
    }

    @Override
    public String description() {
        return "Shows an image of a pet";
    }

    @Override
    public List<? extends OptionData> options() {
        return List.of(
                new OptionData(OptionType.STRING, "name", "The name of the to show", true)
                        .addChoice("lola", "https://media.discordapp.net/attachments/1243043726113116276/1243672737965277285/IMG_6230.png")
                        .addChoice("zeus", "https://media.discordapp.net/attachments/1243043726113116276/1243316817959587890/IMG_4336.jpg")
                        .addChoice("alexander", "https://media.discordapp.net/attachments/1243043726113116276/1253485774335250482/IMG_7804.jpg")
                        .addChoice("lady", "https://media.discordapp.net/attachments/1243043726113116276/1249958703315157083/IMG_6535.jpg")
        );
    }

    @Override
    public void execute(final CommandContext context) {
        final PetOptions petOptions = context.options(PetOptions.class);
        context.reply(petOptions.name);
    }

    public static class PetOptions {
        public String name;
    }
}
