package dev.jaczerob.olivia.common.errors;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.util.WebhookErrorHandler;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
public class GlobalExceptionHandler {
    private final String webhookURL;
    private final WebhookErrorHandler webhookErrorHandler;

    public GlobalExceptionHandler(final @Value("${discord.webhook-url}") String webhookURL, final WebhookErrorHandler webhookErrorHandler) {
        this.webhookURL = webhookURL;
        this.webhookErrorHandler = webhookErrorHandler;
    }

    @Pointcut("execution(* dev.jaczerob.olivia.*.*(..))")
    public void oliviaMethods() {
    }

    @AfterThrowing(pointcut = "oliviaMethods()", throwing = "exc")
    public void handleExceptions(final Throwable exc) {
        final WebhookEmbed webhookEmbed;

        try (final StringWriter stringWriter = new StringWriter(); final PrintWriter printWriter = new PrintWriter(stringWriter);) {
            exc.printStackTrace(printWriter);

            webhookEmbed = new WebhookEmbedBuilder().setColor(0xFF0000).setTitle(new WebhookEmbed.EmbedTitle("Exception: %s".formatted(exc.getClass().getSimpleName()), null)).setDescription(stringWriter.toString()).build();
        } catch (final IOException innerExc) {
            this.webhookErrorHandler.handle(null, innerExc.getMessage(), innerExc);
            return;
        }

        try (final WebhookClient webhookClient = WebhookClient.withUrl(this.webhookURL)) {
            webhookClient.setErrorHandler(this.webhookErrorHandler);
            webhookClient.send(webhookEmbed);
        }
    }
}
