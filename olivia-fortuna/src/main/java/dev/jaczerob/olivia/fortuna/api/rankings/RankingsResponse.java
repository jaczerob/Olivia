package dev.jaczerob.olivia.fortuna.api.rankings;

import java.util.List;

public class RankingsResponse {
    private List<Character> rankings;

    public RankingsResponse() {
    }

    public RankingsResponse(final List<Character> rankings) {
        this.rankings = rankings;
    }

    public List<Character> getRankings() {
        return this.rankings;
    }
}
