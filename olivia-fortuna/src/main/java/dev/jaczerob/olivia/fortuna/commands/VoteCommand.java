package dev.jaczerob.olivia.fortuna.commands;

import dev.jaczerob.olivia.discord.commands.CommandContext;
import dev.jaczerob.olivia.discord.commands.ICommand;
import dev.jaczerob.olivia.fortuna.database.repositories.FortunaRepository;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoteCommand implements ICommand {
    @Autowired
    private FortunaRepository fortunaRepository;

    @Override
    public String name() {
        return "vote";
    }

    @Override
    public String description() {
        return "Get your vote URL";
    }

    @Override
    public boolean ephemeral() {
        return true;
    }

    @Override
    public void execute(CommandContext context) {
        final Member member = context.event().getMember();
        if (member == null) {
            context.reply("failed to get... yourself");
            return;
        }

        final List<String> usernames = this.fortunaRepository.getUsernameByDiscordID(member.getId());
        if (usernames == null || usernames.isEmpty()) {
            context.reply("you don't have an account");
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        usernames.forEach(username -> {
            stringBuilder.append("Vote for %s: <https://gtop100.com/topsites/MapleStory/sitedetails/Fortuna-v179-103846?vote=1&pingUsername=%s>\n".formatted(username, username));
        });

        context.reply(stringBuilder.toString().trim());
    }
}
