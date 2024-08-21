package dev.jaczerob.olivia.fortuna.database.repositories;

import dev.jaczerob.olivia.fortuna.database.models.DiscordDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscordRepository extends MongoRepository<DiscordDocument, String> {

}
