package usecase.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ContractDTO {

    public final String athleteId;
    public final String teamId;
    public final String eventId;
    public final Instant dateSigned;
    public final String leagueId;

}
