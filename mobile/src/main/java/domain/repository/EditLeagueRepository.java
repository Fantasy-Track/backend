package domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface EditLeagueRepository extends LeagueRepository {

    void setDraftTime(String leagueId, Instant draftTime);

    void updateMaxTeams(String leagueId, int maxTeams);

    void setTurnDurationMillis(String leagueId, int millis);

    void setAthletes(String leagueId, List<String> athletes);

}
