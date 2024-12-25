package dev.jaczerob.olivia.fortuna.database.repositories;

import dev.jaczerob.olivia.fortuna.database.models.RankingCharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FortunaRepository extends JpaRepository<RankingCharacterEntity, Integer> {
    @Query(
            nativeQuery = true,
            value = """
                    select count(id) from users u
                    where u.clientstate = 2
                    and u.accounttype = 0;
                    """
    )
    Integer getOnlineCount();

    @Query(
            nativeQuery = true,
            value = """
                    select u.name
                    from users u
                    where discordid = ?1
                    """
    )
    List<String> getUsernameByDiscordID(String discordId);
}
