package dev.jaczerob.olivia.fortuna.database.repositories;

import dev.jaczerob.olivia.fortuna.database.models.DiscordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FortunaDiscordRepository extends JpaRepository<DiscordEntity, String> {
    @Query(
            nativeQuery = true,
            value = """
                    SELECT u.id, u.discordid, cs.name
                      FROM characterstats cs,
                           characters c,
                           users u,
                           accounts a
                      WHERE c.id = cs.id
                        AND c.accid = a.id
                        AND a.userid = u.id
                        AND u.discordid IS NOT null;
                    """
    )
    List<DiscordEntity> getAllCharacters();
}
