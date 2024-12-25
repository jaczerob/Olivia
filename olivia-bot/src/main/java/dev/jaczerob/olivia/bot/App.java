package dev.jaczerob.olivia.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "dev.jaczerob.olivia")
@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(final String... args) {
        SpringApplication.run(App.class, args);
    }
}
