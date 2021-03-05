package domain.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
public class League {

    public final String id;
    public final String owningTeam;
    public final String schoolId;
    public final String name;
    public final List<String> athletes;
    public final int maxTeams;

    public final LeagueSettings leagueSettings;
    public final DraftSettings draftSettings;

    public final LeagueStatus status;

    @Builder
    public static class DraftSettings {
        public final Instant startTime;
        public final int rounds, turnDurationMillis;
    }

    @Builder
    public static class LeagueSettings {
        public final int teamSize;
    }

}