package dev.jaczerob.olivia.ttr;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PopulationResponse {
    private final int totalPopulation;
    private final Map<String, PopulationDistrictResponse> districts;

    public PopulationResponse(
            @JsonProperty("totalPopulation") final int totalPopulation,
            @JsonProperty("populationByDistrict") final Map<String, Integer> populationByDistrict,
            @JsonProperty("statusByDistrict") final Map<String, String> statusByDistrict
    ) {
        this.totalPopulation = totalPopulation;

        final Set<String> districtNames = populationByDistrict.keySet();
        this.districts = districtNames.stream().map(district -> new PopulationDistrictResponse(
                district,
                populationByDistrict.get(district),
                statusByDistrict.get(district)
        )).collect(Collectors.toMap(PopulationDistrictResponse::getName, v -> v));
    }

    public int getTotalPopulation() {
        return this.totalPopulation;
    }

    public Map<String, PopulationDistrictResponse> getDistricts() {
        return this.districts;
    }
}
