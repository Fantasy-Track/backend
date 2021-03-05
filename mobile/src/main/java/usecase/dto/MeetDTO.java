package usecase.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public class MeetDTO {

    public final String id, athleticId, leagueId;
    public final String name;
    public final Instant date;
    public final boolean hasResults;
    public final boolean enabled;
    public final boolean locked;

}
