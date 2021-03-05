package usecase.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ResultDTO {

    public final String id, athleteName, athleteId;
    public final Instant date;
    public final String eventId, mark, meetId, meetName, teamId, teamName, url;
    public final double fantasyPoints;
    public final int rank;

}
