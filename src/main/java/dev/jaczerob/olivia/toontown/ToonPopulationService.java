package dev.jaczerob.olivia.toontown;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ToonPopulationService {
    private final TTRAPIClient ttrapiClient;
    private final MeterRegistry meterRegistry;

    private final AtomicInteger totalToons = new AtomicInteger();
    private final Map<String, Integer> toonsPerDistrict = new ConcurrentHashMap<>();

    public ToonPopulationService(final TTRAPIClient ttrapiClient, final MeterRegistry meterRegistry) {
        this.ttrapiClient = ttrapiClient;

        Gauge.builder("toontown.population", this.totalToons, AtomicInteger::get).tag("district", "all").register(meterRegistry);

        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 60 * 1000)
    private void schedule() {
        final PopulationResponse populationResponse = this.ttrapiClient.getPopulation();
        this.totalToons.set(populationResponse.getTotalPopulation());

        populationResponse.getDistricts().forEach((districtName, districtPopulation) -> {
            final boolean initializeGauge = !this.toonsPerDistrict.containsKey(districtName);
            this.toonsPerDistrict.put(districtName, districtPopulation.getPopulation());

            if (initializeGauge) {
                Gauge.builder("toontown.population", this.toonsPerDistrict, f -> f.get(districtName)).tag("district", districtName).register(this.meterRegistry);
            }
        });
    }
}
