package dev.jaczerob.olivia.fortuna.cron;

import dev.jaczerob.olivia.fortuna.database.models.DiscordDocument;
import dev.jaczerob.olivia.fortuna.database.models.DiscordEntity;
import dev.jaczerob.olivia.fortuna.database.repositories.DiscordRepository;
import dev.jaczerob.olivia.fortuna.database.repositories.FortunaDiscordRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UpdateDiscordUsersCron {
    private static final Logger log = LogManager.getLogger();

    private final DiscordRepository discordRepository;
    private final FortunaDiscordRepository fortunaRepository;
    private final JDA jda;

    public UpdateDiscordUsersCron(
            final DiscordRepository discordRepository,
            final FortunaDiscordRepository fortunaRepository,
            final JDA jda
    ) {
        this.discordRepository = discordRepository;
        this.fortunaRepository = fortunaRepository;
        this.jda = jda;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateDiscordUsers() {
        final Map<String, DiscordDocument> newDiscordDocuments = new ConcurrentHashMap<>();

        final List<DiscordEntity> discordEntities = this.fortunaRepository.getAllCharacters();

        final Map<String, Set<String>> discordIDToCharacters = new HashMap<>();
        discordEntities.forEach(entity -> {
            if (discordIDToCharacters.containsKey(entity.getDiscordid())) {
                final Set<String> existingCharacters = discordIDToCharacters.get(entity.getDiscordid());
                existingCharacters.add(entity.getName());
                discordIDToCharacters.put(entity.getDiscordid(), existingCharacters);
            } else {
                final Set<String> characters = new HashSet<>();
                characters.add(entity.getName());
                discordIDToCharacters.put(entity.getDiscordid(), characters);
            }
        });

        final List<DiscordDocument> existingDiscordDocuments = this.discordRepository.findAll();
        existingDiscordDocuments.stream().parallel().forEach(discordDocument -> {
            if (!discordIDToCharacters.containsKey(discordDocument.getId()))
                return;

            final Set<String> currentFortunaCharacters = discordIDToCharacters.get(discordDocument.getId());
            discordDocument.getFortunaCharacters().addAll(currentFortunaCharacters);
            final Optional<String> discordUsername = this.getDiscordUsername(discordDocument.getId());
            discordUsername.ifPresent(s -> discordDocument.getDiscordUsernames().add(s));

            final DiscordDocument newDiscordDocument = new DiscordDocument();
            newDiscordDocument.setId(discordDocument.getId());
            newDiscordDocument.setFortunaCharacters(discordDocument.getFortunaCharacters());
            discordDocument.getDiscordUsernames().remove("");
            newDiscordDocument.setDiscordUsernames(discordDocument.getDiscordUsernames());
            newDiscordDocuments.put(discordDocument.getId(), newDiscordDocument);
        });

        discordIDToCharacters.entrySet().stream().parallel().forEach(entry -> {
            if (newDiscordDocuments.containsKey(entry.getKey()))
                return;

            final DiscordDocument discordDocument = new DiscordDocument();
            final Optional<String> discordUsername = this.getDiscordUsername(entry.getKey());
            if (discordUsername.isPresent())
                discordDocument.setDiscordUsernames(Set.of(discordUsername.get()));
            else
                discordDocument.setDiscordUsernames(Set.of());
            discordDocument.setFortunaCharacters(entry.getValue());
            discordDocument.setId(entry.getKey());
            newDiscordDocuments.put(entry.getKey(), discordDocument);
        });

        this.discordRepository.saveAll(newDiscordDocuments.values());
        log.info("Saved Discord documents: {}", newDiscordDocuments.size());
    }

    private Optional<String> getDiscordUsername(final String id) {
        try {
            final Member user = this.jda
                    .getGuildById("1269289543304216669")
                    .getTextChannelById("1269289543304216675")
                    .getMembers()
                    .stream().filter(m -> m.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            final String userName;
            if (user != null)
                userName = user.getUser().getName();
            else
                userName = null;
            return Optional.ofNullable(userName);
        } catch (final Throwable exc) {
            log.error(exc);
            return Optional.empty();
        }
    }
}
