package mock;

import domain.entity.Team;

public class TeamBank {

    public static Team TEAM1 = Team.builder()
            .id("T1")
            .name("Team1")
            .leagueId("test")
            .ownerId("O1")
            .fantasyPoints(0)
            .build();

    public static Team TEAM2 = Team.builder()
            .id("T2")
            .name("Team2")
            .leagueId("test")
            .ownerId("O2")
            .fantasyPoints(0)
            .build();

    public static Team OTHER_LEAGUE_TEAM = Team.builder()
            .id("T3")
            .name("Team3")
            .leagueId("L2")
            .ownerId("O3")
            .fantasyPoints(0)
            .build();

}
