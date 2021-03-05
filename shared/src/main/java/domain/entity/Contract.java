package domain.entity;

import lombok.Builder;

import java.time.Instant;

@Builder
public class Contract {

    public final String athleteId, teamId, leagueId, eventId, categoryId;
    public final Instant dateSigned;

    @Override
    public String toString() {
        return "Contract{" +
                "athleteId='" + athleteId + '\'' +
                ", teamId='" + teamId + '\'' +
                '}';
    }
}
