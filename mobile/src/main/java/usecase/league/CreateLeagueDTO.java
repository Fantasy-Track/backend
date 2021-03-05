package usecase.league;

import lombok.Builder;

@Builder
public class CreateLeagueDTO {
    public final String leagueId;
    public final String owningTeamId;
}
