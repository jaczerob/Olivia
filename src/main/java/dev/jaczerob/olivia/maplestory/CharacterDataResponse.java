package dev.jaczerob.olivia.maplestory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterDataResponse {
    @JsonProperty("CharacterData")
    public CharacterData characterData;
}
