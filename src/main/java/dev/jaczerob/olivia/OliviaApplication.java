package dev.jaczerob.olivia;

import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.Duration;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class OliviaApplication {
    private static final Logger log = LoggerFactory.getLogger(OliviaApplication.class);

    static {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

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

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.ALL));
        return mappingJackson2HttpMessageConverter;
    }
}
