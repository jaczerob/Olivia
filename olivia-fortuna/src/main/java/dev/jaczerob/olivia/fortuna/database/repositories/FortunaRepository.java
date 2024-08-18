package dev.jaczerob.olivia.fortuna.database.repositories;

import dev.jaczerob.olivia.fortuna.database.models.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FortunaRepository extends JpaRepository<CharacterEntity, Integer> {
    @Query(
            nativeQuery = true,
            value = """
                    SELECT cs.id, cs.name, cs.level, cs.job
                    FROM characterstats cs, characters c, users u
                    WHERE c.id = cs.id AND c.accid = u.id AND u.accounttype = 0 AND (u.banExpireDate IS null OR u.banExpireDate <= NOW())
                    ORDER BY cs.level DESC
                    LIMIT ?1;
                    """
    )
    List<CharacterEntity> getTopPlayers(Integer amount);
}
