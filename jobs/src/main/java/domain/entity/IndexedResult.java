package domain.entity;

import lombok.Builder;

import java.time.Instant;

@Builder
public class IndexedResult {

    public final String url;
    public final String athleteId;
    public final String eventId;
    public final String meetId;
    public final String leagueId;
    public final Instant date;
    public final Mark mark;

    @Override
    public String toString() {
        return String.format("url: %s, Event ID: %s, " +
                "Mark: %s, Athlete ID: %s," +
                " Meet ID: %s", url, eventId, mark, athleteId, meetId);
    }

}
