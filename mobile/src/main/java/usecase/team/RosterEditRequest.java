package usecase.team;

import lombok.Builder;

@Builder
public class RosterEditRequest {

    public final String athleteId;
    public final String teamId;
    public final String leagueId;
    public final String eventId;

}
