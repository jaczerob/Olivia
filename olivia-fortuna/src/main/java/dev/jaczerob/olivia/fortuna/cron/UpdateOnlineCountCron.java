package dev.jaczerob.olivia.fortuna.cron;

import dev.jaczerob.olivia.fortuna.api.FortunaAPIClient;
import dev.jaczerob.olivia.fortuna.api.online.OnlineResponse;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UpdateOnlineCountCron {
    private static final Logger log = LogManager.getLogger();

    private final JDA jda;
    private final FortunaAPIClient fortunaAPIClient;
    private final String onlinePlayersChannelID;
    private final MeterRegistry meterRegistry;

    private final AtomicInteger onlinePlayers = new AtomicInteger();
    private final Map<String, Integer> onlinePlayersPerChannel = new ConcurrentHashMap<>();

    public UpdateOnlineCountCron(
            final JDA jda,
            final FortunaAPIClient fortunaAPIClient,
            final @Value("${fortuna.online-players-channel-id}") String onlinePlayersChannelID,
            final MeterRegistry meterRegistry
    ) {
        this.jda = jda;
        this.fortunaAPIClient = fortunaAPIClient;
        this.onlinePlayersChannelID = onlinePlayersChannelID;
        this.meterRegistry = meterRegistry;

        Gauge.builder("fortuna.players.online", this.onlinePlayers::get)
                .tag("channel", "all")
                .register(meterRegistry);
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
            this.onlinePlayers.set(onlinePlayers);
            serverOnline = true;

            onlineResponse.getOnlineCount().getChannels().forEach((channelName, population) -> {
                final boolean initializeGauge = !this.onlinePlayersPerChannel.containsKey(channelName);
                this.onlinePlayersPerChannel.put(channelName, population);

                if (initializeGauge) {
                    Gauge.builder("fortuna.players.online", this.onlinePlayersPerChannel, f -> f.get(channelName)).tag("channel", channelName).register(this.meterRegistry);
                }
            });
        }

        final String status;
        if (serverOnline) {
            status = "Online Players: %d".formatted(onlinePlayers);
        } else {
            status = "Server Offline";
        }

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
