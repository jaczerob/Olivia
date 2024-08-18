package dev.jaczerob.olivia.toonhq;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ToonHQScraper {
    private static final Logger log = LogManager.getLogger();

    private static final Pattern TOON_PATTERN = Pattern.compile("\"toon\": (.+?})", Pattern.DOTALL);
    private static final String TOON_HQ_URL = "https://toonhq.org/groups";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AtomicInteger toonsyncedToons = new AtomicInteger();
    private final AtomicInteger nonToonsyncedToons = new AtomicInteger();
    private final ToonRepository toonRepository;

    public ToonHQScraper(final MeterRegistry meterRegistry, final ToonRepository toonRepository) {
        Gauge.builder("toonhq_number_toons", this.toonsyncedToons, AtomicInteger::get)
                .tag("type", "toonsync")
                .register(meterRegistry)
                .measure();

        Gauge.builder("toonhq_number_toons", this.nonToonsyncedToons, AtomicInteger::get)
                .tag("type", "non-toonsync")
                .register(meterRegistry)
                .measure();

        this.toonRepository = toonRepository;
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
        int nonToonsycnedToons = 0;
        final List<ToonHQToon> toons = new ArrayList<>();

        while (toonMatcher.find()) {
            final String toonJson = toonMatcher.group(1);
            final ToonHQToon toonHQToon;

            try {
                toonHQToon = OBJECT_MAPPER.readValue(toonJson, ToonHQToon.class);
            } catch (final JsonProcessingException exc) {
                log.error("Could not parse ToonHQ Toon: {}", toonJson, exc);
                continue;
            }

            if (toonHQToon.getGame() != 1) {
                continue;
            } else if (toonHQToon.getId() == 0 || toonHQToon.getPhoto() == null) {
                nonToonsycnedToons++;
                continue;
            }

            toons.add(toonHQToon);
        }

        this.nonToonsyncedToons.set(nonToonsycnedToons);
        this.toonsyncedToons.set(toons.size());
        this.toonRepository.saveAll(toons);
        log.info("Scraped {} toons from ToonHQ", toons.size());
    }
}
