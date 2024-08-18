package dev.jaczerob.olivia.common.config;

import club.minnced.discord.webhook.util.WebhookErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
    private static final Logger log = LogManager.getLogger();

    @Bean
    public WebhookErrorHandler webhookErrorHandler() {
        return (webhookClient, s, throwable) -> log.error(s, throwable);
    }
}
