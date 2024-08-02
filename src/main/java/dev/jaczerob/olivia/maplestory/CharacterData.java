package dev.jaczerob.olivia.maplestory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterData {
    @JsonProperty("AchievementPoints")
    public int achievementPoints;
    @JsonProperty("AchievementRank")
    public int achievementRank;
    @JsonProperty("CharacterImageURL")
    public String characterImageURL;
    @JsonProperty("Class")
    public String clazz;
    @JsonProperty("ClassRank")
    public int clazzRank;
    @JsonProperty("EXP")
    public long eXP;
    @JsonProperty("EXPPercent")
    public double eXPPercent;
    @JsonProperty("GlobalRanking")
    public int globalRanking;
    @JsonProperty("LegionCoinsPerDay")
    public int legionCoinsPerDay;
    @JsonProperty("LegionLevel")
    public int legionLevel;
    @JsonProperty("LegionPower")
    public int legionPower;
    @JsonProperty("LegionRank")
    public int legionRank;
    @JsonProperty("Level")
    public int level;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Server")
    public String server;
    @JsonProperty("ServerClassRanking")
    public int serverClassRanking;
    @JsonProperty("ServerRank")
    public int serverRank;
}
