package fantasyapp.repository;

import fantasyapp.dao.AthleteDAO;
import fantasyapp.dao.LeagueDAO;
import fantasyapp.repository.mapper.AthleteMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.entity.Athlete;
import domain.repository.AthleteRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DBAthleteRepository implements AthleteRepository {

    private MongoCollection<AthleteDAO> athleteCollection;
    private MongoCollection<LeagueDAO> leagueCollection;

    @Inject
    public DBAthleteRepository(MongoDatabase database) {
        leagueCollection = database.getCollection("leagues", LeagueDAO.class);
        athleteCollection = database.getCollection("athletes", AthleteDAO.class);
    }

    @Override
    public Athlete getAthleteById(String id) {
        AthleteDAO dao = athleteCollection.find(eq("_id", id)).first();
        if (dao == null) return null;
        return AthleteMapper.daoToAthlete(dao);
    }

    @Override
    public List<Athlete> getAthletesByIds(List<String> ids) {
        List<Athlete> athletes = new ArrayList<>();
        for (AthleteDAO dao : athleteCollection.find(in("_id", ids))) {
            athletes.add(AthleteMapper.daoToAthlete(dao));
        }
        return athletes;
    }

    @Override
    public boolean isAthleteInLeague(String athleteId, String leagueId) {
        LeagueDAO dao = leagueCollection.find(and(eq("_id", leagueId), in("athletes", athleteId))).first();
        return dao != null;
    }

}
