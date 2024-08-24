package dev.jaczerob.olivia.fortuna.staff.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DiscordRepository extends MongoRepository<DiscordDocument, String> {
    @Query("{fortunaCharacters:  ?0}")
    Optional<DiscordDocument> findByCharacterName(String characterName);

    @Query("{_id:  ?0}")
    Optional<DiscordDocument> findByDiscordID(String discordID);

    @Query("{discordUsernames:  ?0}")
    Optional<DiscordDocument> findByDiscordUsername(String discordUsername);
}
