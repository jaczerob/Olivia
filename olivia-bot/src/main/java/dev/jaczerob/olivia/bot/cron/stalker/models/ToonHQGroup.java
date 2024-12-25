package dev.jaczerob.olivia.bot.cron.stalker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToonHQGroup {
    private String id;
    private int type;
    private int district;
    private int location;

    private List<ToonHQGroupMember> members;

    public ToonHQGroup() {
    }

    public List<ToonHQGroupMember> getMembers() {
        return this.members;
    }

    public void setMembers(List<ToonHQGroupMember> members) {
        this.members = members;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDistrict() {
        return this.district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
