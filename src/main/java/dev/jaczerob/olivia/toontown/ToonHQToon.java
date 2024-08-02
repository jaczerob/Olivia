package dev.jaczerob.olivia.toontown;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ToonHQToon(
        int id,
        int game,
        String photo,
        int species,
        int laff,
        int toonup,
        int trap,
        int lure,
        int sound,
        @JsonProperty("throw")
        int throwGag,
        int squirt,
        int drop,
        List<String> prestiges,
        String sellbot,
        String cashbot,
        String lawbot,
        String bossbot
) {
    public String organic() {
        if (this.prestiges().isEmpty())
            return "none";
        else
            return this.prestiges.get(0).toLowerCase();
    }
}