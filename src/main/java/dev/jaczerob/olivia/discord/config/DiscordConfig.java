package dev.jaczerob.olivia.discord.config;

import dev.jaczerob.olivia.discord.commands.CommandHandler;
import dev.jaczerob.olivia.discord.commands.ICommand;
import io.micrometer.core.instrument.MeterRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
public class DiscordConfig {
    @Bean
    public ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        final ThreadFactory threadFactory = Thread.ofVirtual().factory();
        final int cores = Runtime.getRuntime().availableProcessors();
        return Executors.newScheduledThreadPool(cores, threadFactory);
    }

    @Bean
    public JDA jda(
            final @Value("${discord.token:}") String token,
            final @Value("${discord.status.type}") Activity.ActivityType activityType,
            final @Value("${discord.status.message}") String activityMessage,
            final ExecutorService executorService,
            final ScheduledExecutorService scheduledExecutorService,
            final List<ICommand> commands,
            final MeterRegistry meterRegistry
    ) {
        return JDABuilder.createLight(token)

                .addEventListeners(new CommandHandler(commands, meterRegistry))

                .disableIntents(Arrays.asList(GatewayIntent.values()))

                .setEventPool(executorService)
                .setCallbackPool(executorService)
                .setGatewayPool(scheduledExecutorService)
                .setAudioPool(scheduledExecutorService)

                .setActivity(Activity.playing("guts"))
                .setActivity(Activity.of(activityType, activityMessage))

                .build();
    }
}