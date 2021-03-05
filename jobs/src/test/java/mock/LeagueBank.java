package mock;

import domain.entity.League;
import domain.entity.LeagueStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LeagueBank {

    public static final League postDraftLeague = League.builder()
            .id("draftedLeague")
            .owningTeam("T1")
            .athletes(List.of("A1", "A2"))
            .status(LeagueStatus.POST_DRAFT)
            .maxTeams(2)
            .leagueSettings(League.LeagueSettings.builder().teamSize(1).build())
            .build();

    public static final League predraftLeague = League.builder()
            .id("test")
            .owningTeam("T1")
            .status(LeagueStatus.PRE_DRAFT)
            .draftSettings(League.DraftSettings.builder()
                    .startTime(Instant.now().plus(1, ChronoUnit.DAYS))
                    .build())
            .build();

}
