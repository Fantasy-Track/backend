package fantasyapp.repository;

import fantasyapp.dao.ResultDAO;
import fantasyapp.repository.mapper.ResultMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.entity.Result;
import domain.repository.ResultRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DBResultRepository implements ResultRepository {

    private MongoCollection<ResultDAO> resultCollection;

    @Inject
    public DBResultRepository(MongoDatabase database) {
        resultCollection = database.getCollection("results", ResultDAO.class);
    }

    @Override
    public List<Result> getTeamResultsAtMeet(String teamId, String meetId) {
        Iterable<ResultDAO> daos = resultCollection.find(and(eq("teamId", teamId), eq("meetId", meetId)));
        return daosToResults(daos);
    }

    @Override
    public List<Result> getAthleteResultsInLeague(String athleteId, String leagueId) {
        Iterable<ResultDAO> daos = resultCollection.find(and(eq("athleteId", athleteId), eq("leagueId", leagueId)));
        return daosToResults(daos);
    }

    @Override
    public List<Result> getResultsAtMeet(String meetId) {
        Iterable<ResultDAO> daos = resultCollection.find(eq("meetId", meetId));
        return daosToResults(daos);
    }

    private List<Result> daosToResults(Iterable<ResultDAO> daos) {
        List<Result> results = new ArrayList<>();
        for (ResultDAO dao : daos) {
            results.add(ResultMapper.daoToResult(dao));
        }
        return results;
    }


}
