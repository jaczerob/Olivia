package dev.jaczerob.olivia.toontown;

public class PopulationDistrictResponse {
    private String name;
    private int population;
    private String status;

    public PopulationDistrictResponse() {
    }

    public PopulationDistrictResponse(final String name, final int population, final String status) {
        this.name = name;
        this.population = population;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public int getPopulation() {
        return this.population;
    }

    public String getStatus() {
        return this.status;
    }
}
