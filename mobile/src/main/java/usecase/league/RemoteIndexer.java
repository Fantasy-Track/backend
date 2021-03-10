package usecase.league;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface RemoteIndexer {

    List<String> indexAthletesInSchool(String schoolId) throws Exception;

    void indexMeets(String schoolId, String leagueId, Instant afterDate) throws Exception;

    void rescoreMeet(String meetId) throws Exception;

}
