package usecase.league;

import domain.exception.MissingArgument;
import lombok.Builder;

import java.time.Instant;

@Builder
public class CreateLeagueRequest {
    public final String name;
    public final String ownerId;
    public final String teamName;
    public final String schoolId;
    public final Instant draftTime;

    public void validateNoNulls() throws MissingArgument {
        if (name == null || ownerId == null || teamName == null || schoolId == null || draftTime == null) {
            throw new MissingArgument();
        }
    }

}
