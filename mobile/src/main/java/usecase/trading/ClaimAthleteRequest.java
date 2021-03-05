package usecase.trading;

import lombok.Builder;

@Builder
public class ClaimAthleteRequest {

    public final String teamId;
    public final String athleteId;
    public final String leagueId;

}
