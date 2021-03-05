package domain.entity;

import lombok.Builder;

import java.time.Instant;

@Builder
public class Result {

    public final String id;
    public final String athleteId, eventId, mark, meetId, teamId, url;
    public final Instant date;
    public final double fantasyPoints;
    public final int rank;

}
