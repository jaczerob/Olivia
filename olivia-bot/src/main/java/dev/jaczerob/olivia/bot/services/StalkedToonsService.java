package dev.jaczerob.olivia.bot.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jaczerob.olivia.bot.cron.stalker.models.StalkedToon;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class StalkedToonsService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Path STALKED_TOONS_PATH = Path.of("discord-data", "stalked-toons.json");

    private List<StalkedToon> toons;

    @PostConstruct
    public void loadToons() throws Exception {
        if (STALKED_TOONS_PATH.toFile().exists()) {
            this.toons = OBJECT_MAPPER.readValue(STALKED_TOONS_PATH.toFile(), new TypeReference<>() {
            });
        } else {
            this.toons = new ArrayList<>();
        }
    }

    @PreDestroy
    public void saveToons() throws Exception {
        OBJECT_MAPPER.writeValue(STALKED_TOONS_PATH.toFile(), this.toons);
    }

    public List<StalkedToon> getToons() {
        return this.toons;
    }

    public void addToon(final StalkedToon toon) {
        this.toons.add(toon);
    }

    public boolean removeToon(final StalkedToon toon) {
        return this.toons.remove(toon);
    }
}
