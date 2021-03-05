package usecase.trading;

import lombok.Builder;

import java.util.List;

@Builder
public class DropAthletesRequest {

    public final String teamId;
    public final List<String> athleteIds;
    public final String leagueId;

    @Override
    public String toString() {
        return "DropAthletesRequest{" +
                "teamId='" + teamId + '\'' +
                ", athleteIds='" + athleteIds + '\'' +
                '}';
    }
}
