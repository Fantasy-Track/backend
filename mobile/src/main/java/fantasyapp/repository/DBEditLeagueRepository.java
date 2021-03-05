package fantasyapp.repository;

import fantasyapp.dao.LeagueDAO;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.repository.EditLeagueRepository;

import java.time.Instant;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DBEditLeagueRepository extends DBLeagueRepository implements EditLeagueRepository {

    private MongoCollection<LeagueDAO> leagueCollection;

    @Inject
    public DBEditLeagueRepository(MongoDatabase database) {
        super(database);
        leagueCollection = database.getCollection("leagues", LeagueDAO.class);
    }

    @Override
    public void setDraftTime(String leagueId, Instant draftTime) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("draftSettings.startTime", draftTime));
    }

    @Override
    public void updateMaxTeams(String leagueId, int maxTeams) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("maxTeams", maxTeams));
    }

    @Override
    public void setTurnDurationMillis(String leagueId, int millis) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("draftSettings.turnDurationMillis", millis));
    }

    @Override
    public void setAthletes(String leagueId, List<String> athletes) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("athletes", athletes));
    }

}
