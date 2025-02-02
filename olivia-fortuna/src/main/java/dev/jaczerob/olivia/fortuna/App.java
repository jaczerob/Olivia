package dev.jaczerob.olivia.fortuna;

import feign.Retryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@ComponentScan(basePackages = "dev.jaczerob.olivia")

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class App {
    public static void main(final String... args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.ALL));
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100L, 3 * 1000L, 10);
    }
}