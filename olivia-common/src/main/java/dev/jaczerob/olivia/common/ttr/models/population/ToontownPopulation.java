package dev.jaczerob.olivia.common.ttr.models.population;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ToontownPopulation implements Serializable {
    private final int totalPopulation;
    private final Map<String, DistrictPopulation> districts;

    public ToontownPopulation(
            @JsonProperty("totalPopulation") final int totalPopulation,
            @JsonProperty("populationByDistrict") final Map<String, Integer> populationByDistrict,
            @JsonProperty("statusByDistrict") final Map<String, String> statusByDistrict
    ) {
        this.totalPopulation = totalPopulation;

        final Set<String> districtNames = populationByDistrict.keySet();
        this.districts = districtNames.stream().map(district -> new DistrictPopulation(
                district,
                populationByDistrict.get(district),
                statusByDistrict.get(district)
        )).collect(Collectors.toMap(DistrictPopulation::getName, v -> v));
    }

    public int getTotalPopulation() {
        return this.totalPopulation;
    }

    public Map<String, DistrictPopulation> getDistricts() {
        return this.districts;
    }

    @Override
    public String toString() {
        return "ToontownPopulation[totalPopulation=%d, districts=%s]".formatted(this.totalPopulation, this.districts);
    }
}
