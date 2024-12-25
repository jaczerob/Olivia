package dev.jaczerob.olivia.bot.cron.stalker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToonHQState {
    private final Map<Integer, String> districts = new HashMap<>();
    private final Map<Integer, String> locations = new HashMap<>();
    private final Map<Integer, String> groupTypes = new HashMap<>();
    private List<ToonHQGroup> groups;

    public ToonHQState() {
    }

    public List<ToonHQGroup> getGroups() {
        return this.groups;
    }

    public void setGroups(List<ToonHQGroup> groups) {
        this.groups = groups;
    }

    public Map<Integer, String> getDistricts() {
        return this.districts;
    }

    @JsonSetter("districts")
    public void setDistricts(List<District> districts) {
        districts.forEach(district -> this.districts.put(district.getId(), district.getName()));
    }

    public Map<Integer, String> getLocations() {
        return this.locations;
    }

    @JsonSetter("locations")
    public void setLocations(List<Location> locations) {
        locations.forEach(location -> this.locations.put(location.getId(), location.getName()));
    }

    public Map<Integer, String> getGroupTypes() {
        return this.groupTypes;
    }

    @JsonSetter("group_types")
    public void setGroupTypes(List<GroupType> groupTypes) {
        groupTypes.forEach(groupType -> this.groupTypes.put(groupType.getId(), groupType.getName()));
    }
}
