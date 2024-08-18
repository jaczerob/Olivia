package dev.jaczerob.olivia.fortuna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "dev.jaczerob.olivia")
@SpringBootApplication
public class App {
    public static void main(final String... args) throws ClassNotFoundException {
        SpringApplication.run(App.class, args);
    }
}