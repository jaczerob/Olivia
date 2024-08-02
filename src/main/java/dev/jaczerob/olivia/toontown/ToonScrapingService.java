package dev.jaczerob.olivia.toontown;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ToonScrapingService {
    private static final Logger log = LogManager.getLogger();

    private static final Pattern TOON_PATTERN = Pattern.compile("\"toon\": (.+?})", Pattern.DOTALL);
    private static final String TOON_HQ_URL = "https://toonhq.org/groups";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AtomicInteger numberToons = new AtomicInteger();

    public ToonScrapingService(final MeterRegistry meterRegistry) {
        Gauge.builder("toonhq_number_toons", this.numberToons, AtomicInteger::get)
                .tag("type", "toonsync")
                .register(meterRegistry)
                .measure();
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void findToons() {
        final Document document;

        try {
            document = Jsoup.connect(TOON_HQ_URL).get();
        } catch (final IOException exc) {
            log.error("Could not connect to ToonHQ at: {}", TOON_HQ_URL, exc);
            return;
        }

        final Matcher toonMatcher = TOON_PATTERN.matcher(document.html());
        int newNumberToons = 0;

        while (toonMatcher.find()) {
            final String toonJson = toonMatcher.group(1);
            final ToonHQToon toonHQToon;

            try {
                toonHQToon = OBJECT_MAPPER.readValue(toonJson, ToonHQToon.class);
            } catch (final JsonProcessingException exc) {
                log.error("Could not parse ToonHQ Toon: {}", toonJson, exc);
                continue;
            }

            if (toonHQToon.id() == 0 || toonHQToon.photo() == null || toonHQToon.game() != 1) {
                continue;
            }

            newNumberToons++;
        }

        log.info("Scraped {} toons from ToonHQ", newNumberToons);
        this.numberToons.set(newNumberToons);
    }
}
