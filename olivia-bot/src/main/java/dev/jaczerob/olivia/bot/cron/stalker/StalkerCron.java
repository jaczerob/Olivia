package dev.jaczerob.olivia.bot.cron.stalker;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jaczerob.olivia.bot.cron.stalker.models.*;
import dev.jaczerob.olivia.bot.services.StalkedToonsService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StalkerCron {
    private static final Logger log = LogManager.getLogger();

    private static final Pattern STATE_PATTERN = Pattern.compile("window\\.STATE = (\\{[^;]+;)", Pattern.DOTALL);
    private static final String TOON_HQ_URL = "https://toonhq.org/groups";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    @Autowired
    private StalkedToonsService stalkedToonsService;

    @Autowired
    private JDA jda;

    private static boolean isStalkedToon(final ToonHQToon targetToon, final StalkedToon stalkedToon) {
        return stalkedToon.name().equalsIgnoreCase(targetToon.getName()) &&
                stalkedToon.species().equalsIgnoreCase(targetToon.getSpecies()) &&
                stalkedToon.laff() <= targetToon.getLaff();
    }

    @Scheduled(fixedRate = 15 * 1000)
    public void findToons() throws Exception {
        final Document document;

        try {
            document = Jsoup.connect(TOON_HQ_URL).get();
        } catch (final IOException exc) {
            log.error("Could not connect to ToonHQ at: {}", TOON_HQ_URL, exc);
            return;
        }

        final Matcher stateMatcher = STATE_PATTERN.matcher(document.html());
        if (!stateMatcher.find()) {
            log.error("could not find window state");
            return;
        }

        final String stateJson = stateMatcher.group(1);
        final ToonHQState state = OBJECT_MAPPER.readValue(stateJson, ToonHQState.class);
        if (state == null || state.getGroups() == null || state.getGroups().isEmpty()) {
            log.error("could not get groups from toonhq");
            return;
        }

        final Set<ToonHQToonRef> stalkedToons = new HashSet<>();
        state.getGroups().forEach(group -> group.getMembers().forEach(member -> {
            final Optional<StalkedToon> optionalStalkedToon = isStalkedToon(member.getToon());
            if (optionalStalkedToon.isEmpty()) return;

            final StalkedToon stalkedToon = optionalStalkedToon.get();
            final ToonHQToonRef toonHQToonRef = new ToonHQToonRef(stalkedToon.name(), stalkedToon.species(), stalkedToon.laff());
            toonHQToonRef.setGroup(group);
            stalkedToons.add(toonHQToonRef);
        }));

        if (stalkedToons.isEmpty()) {
            log.info("could not find any stalked toons");
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stalkedToons.forEach(toon -> {
            final ToonHQGroup group = toon.getGroup();
            if (group == null) {
                stringBuilder.append("%s is somewhere on ToonHQ!\n".formatted(
                        toon.name()
                ));

                return;
            }

            if (CACHE.containsKey(toon.name()) && CACHE.get(toon.name()).equals(group.getId())) {
                return;
            }

            CACHE.put(toon.name(), group.getId());
            final String groupType = state.getGroupTypes().getOrDefault(group.getType(), "NULL");
            final String location = state.getLocations().getOrDefault(group.getLocation(), "NULL");
            final String district = state.getDistricts().getOrDefault(group.getDistrict(), "NULL");

            stringBuilder.append("%s is doing a %s at %s %s! <https://toonhq.org/groups/%s>\n".formatted(
                    toon.name(),
                    groupType,
                    district,
                    location,
                    group.getId()
            ));
        });

        if (stringBuilder.toString().isEmpty()) {
            return;
        }

        final TextChannel textChannel = this.jda.getTextChannelById("1278853171367641138");
        if (textChannel == null) {
            log.error("could not get text channel");
        } else {
            textChannel.sendMessage(stringBuilder.toString()).queue();
        }
    }

    private Optional<StalkedToon> isStalkedToon(final ToonHQToon toonHQToon) {
        return this.stalkedToonsService.getToons().stream().filter(stalkedToon -> isStalkedToon(toonHQToon, stalkedToon)).findFirst();
    }
}
