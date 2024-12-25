package dev.jaczerob.olivia.bot.cron.stalker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class ToonHQToonRef {
    private final String name;
    private final String species;
    private final int laff;
    @JsonIgnore
    private ToonHQGroup group = null;

    public ToonHQToonRef(final String name, final String species, final int laff) {
        this.name = name;
        this.species = species;
        this.laff = laff;
    }

    public String name() {
        return this.name;
    }

    public int laff() {
        return this.laff;
    }

    public String species() {
        return this.species;
    }

    public ToonHQGroup getGroup() {
        return this.group;
    }

    public void setGroup(ToonHQGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "ToonRef[name=%s, species=%s, laff=%d, group=%s]".formatted(this.name, this.species, this.laff, this.group == null ? "null" : this.group.getId());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ToonHQToonRef && obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name) + Objects.hashCode(this.species) + laff;
    }
}
