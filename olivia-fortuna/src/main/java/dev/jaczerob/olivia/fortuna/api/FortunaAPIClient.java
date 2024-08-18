package dev.jaczerob.olivia.fortuna.api;

import dev.jaczerob.olivia.fortuna.api.online.OnlineResponse;
import dev.jaczerob.olivia.fortuna.api.rankings.RankingsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "fortuna", url = "https://api.fortunams.org:8080/api/")
public interface FortunaAPIClient {
    @RequestMapping("/server/statistics")
    OnlineResponse getOnlineCount();

    @RequestMapping("/server/rankings")
    RankingsResponse getRankings();
}

