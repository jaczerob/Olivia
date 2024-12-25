package dev.jaczerob.olivia.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
public class DiscordConfig {
    @Bean("normal")
    public ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean("scheduled")
    public ScheduledExecutorService scheduledExecutorService() {
        final ThreadFactory threadFactory = Thread.ofVirtual().factory();
        final int cores = Runtime.getRuntime().availableProcessors();
        return Executors.newScheduledThreadPool(cores, threadFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public JDA jda(
            final @Value("${discord.token}") String token,
            final @Value("${discord.status.type}") Activity.ActivityType activityType,
            final @Value("${discord.status.message}") String activityMessage,
            final @Value("${discord.intents:}") List<GatewayIntent> gatewayIntents,
            final @Value("${discord.cache.members:NONE}") MemberCachePolicy memberCachePolicy,

            final @Qualifier("normal") ExecutorService executorService,
            final @Qualifier("scheduled") ScheduledExecutorService scheduledExecutorService,
            final List<ListenerAdapter> listeners
    ) {
        return JDABuilder.create(gatewayIntents)

                .setToken(token)

                .setEventPassthrough(true)
                .addEventListeners(listeners.toArray())

                .setMemberCachePolicy(memberCachePolicy)

                .setEventPool(executorService)
                .setCallbackPool(executorService)
                .setGatewayPool(scheduledExecutorService)
                .setAudioPool(scheduledExecutorService)

                .setActivity(Activity.of(activityType, activityMessage))

                .build();
    }
}
