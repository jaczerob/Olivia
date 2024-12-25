package dev.jaczerob.olivia.common.ttr.models.population;

import java.io.Serializable;

public class DistrictPopulation implements Serializable {
    private String name;
    private int population;
    private String status;

    public DistrictPopulation() {
    }

    public DistrictPopulation(final String name, final int population, final String status) {
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

    @Override
    public String toString() {
        return "DistrictPopulation[name=%s, population=%d, status=%s]".formatted(this.name, this.population, this.status);
    }
}
