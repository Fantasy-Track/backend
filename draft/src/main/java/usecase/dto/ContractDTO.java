package usecase.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ContractDTO {

    public final String teamName;
    public final String athleteName;
    public final String athleteId;
    public final String teamId;
    public final Instant dateSigned;
}
