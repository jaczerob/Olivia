package dev.jaczerob.olivia.bot;

import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class OliviaApplication {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private JDA jda;

    public static void main(final String... args) {
        SpringApplication.run(OliviaApplication.class, args);
    }

    @PreDestroy
    public void onExit() throws Exception {
        final boolean didShutdown = this.jda.awaitShutdown(Duration.ofMinutes(1));
        log.info("Shut down JDA: {}", didShutdown);
    }
}
