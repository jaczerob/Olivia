package dev.jaczerob.olivia.fortuna.cron;

import dev.jaczerob.olivia.fortuna.database.repositories.FortunaRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateOnlineCountCron {
    private static final Logger log = LogManager.getLogger();

    private final JDA jda;
    private final FortunaRepository fortunaRepository;
    private final String onlinePlayersChannelID;

    public UpdateOnlineCountCron(
            final JDA jda,
            final FortunaRepository fortunaRepository,
            final @Value("${fortuna.online-players-channel-id}") String onlinePlayersChannelID
    ) {
        this.jda = jda;
        this.fortunaRepository = fortunaRepository;
        this.onlinePlayersChannelID = onlinePlayersChannelID;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateOnlineCount() {
        final int onlinePlayers;

        try {
            onlinePlayers = this.fortunaRepository.getOnlineCount();
        } catch (final Throwable exc) {
            log.error("error getting online count from Fortuna", exc);
            return;
        }

        final String status = "Online Players: %d".formatted(onlinePlayers);

        this.jda.getPresence().setActivity(Activity.customStatus(status));
        log.info("Updated presence to: {}", status);

        final VoiceChannel onlinePlayersVoiceChannel = this.jda.getVoiceChannelById(this.onlinePlayersChannelID);
        if (onlinePlayersVoiceChannel == null) {
            log.error("Could not find online count voice channel");

        } else {
            onlinePlayersVoiceChannel.getManager().setName(status).queue();
            log.info("Updated voice channel to: {}", status);
        }
    }
}
