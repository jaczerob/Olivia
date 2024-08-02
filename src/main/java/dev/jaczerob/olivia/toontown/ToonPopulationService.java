package dev.jaczerob.olivia.toontown;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ToonPopulationService {
    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private TTRAPIClient ttrapiClient;

    @Scheduled(fixedRate = 60 * 1000)
    private void schedule() {
        final PopulationResponse populationResponse = this.ttrapiClient.getPopulation();

        Gauge.builder("toontown.population", populationResponse::getTotalPopulation)
                .tag("district", "all")
                .register(this.meterRegistry);

        populationResponse.getDistricts().forEach((district, districtPopulation) ->
                Gauge.builder("toontown.population", districtPopulation::getPopulation)
                        .tag("district", district)
                        .register(this.meterRegistry)
        );
    }
}
