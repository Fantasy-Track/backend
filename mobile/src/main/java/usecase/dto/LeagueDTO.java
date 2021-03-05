package usecase.dto;

import domain.entity.LeagueStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public class LeagueDTO {

    public final String id;
    public final String name;
    public final String draftIPAddress;
    public final Instant draftTime;
    public final int maxTeams;
    public final int numTeams;
    public final LeagueStatus status;
    public final String owningTeamId;
    public final int turnDuration;

}
