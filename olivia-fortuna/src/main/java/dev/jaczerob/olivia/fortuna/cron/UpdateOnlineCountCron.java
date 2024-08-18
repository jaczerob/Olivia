package dev.jaczerob.olivia.fortuna.cron;

import dev.jaczerob.olivia.fortuna.api.FortunaAPIClient;
import dev.jaczerob.olivia.fortuna.api.online.OnlineResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateOnlineCountCron {
    private static final long ONLINE_PLAYERS_CHANNEL_ID = 1274811055138410617L;

    private static final Logger log = LogManager.getLogger();

    private final JDA jda;
    private final FortunaAPIClient fortunaAPIClient;

    public UpdateOnlineCountCron(
            final JDA jda,
            final FortunaAPIClient fortunaAPIClient
    ) {
        this.jda = jda;
        this.fortunaAPIClient = fortunaAPIClient;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateOnlineCount() {
        final OnlineResponse onlineResponse = this.fortunaAPIClient.getOnlineCount();
        final int onlinePlayers;
        final boolean serverOnline;
        if (onlineResponse == null || onlineResponse.getOnlineCount() == null) {
            log.error("Could not get online count from Fortuna");
            onlinePlayers = 0;
            serverOnline = false;
        } else {
            onlinePlayers = onlineResponse.getOnlineCount().getUnique();
            serverOnline = true;
        }

        final String status;
        if (serverOnline) {
            status = "Online Players: %d".formatted(onlinePlayers);
        } else {
            status = "Server Offline";
        }

        this.jda.getPresence().setActivity(Activity.customStatus(status));
        log.info("Updated presence to: {}", status);

        final VoiceChannel onlinePlayersVoiceChannel = this.jda.getVoiceChannelById(ONLINE_PLAYERS_CHANNEL_ID);
        if (onlinePlayersVoiceChannel == null) {
            log.error("Could not find online count voice channel");

        } else {
            onlinePlayersVoiceChannel.getManager().setName(status).queue();
            log.info("Updated voice channel to: {}", status);
        }
    }
}
