package usecase.createTeam;

import lombok.Builder;

@Builder
public class CreateTeamRequest {

    public final String ownerId;
    public final String teamName;
    public final String leagueId;

}
