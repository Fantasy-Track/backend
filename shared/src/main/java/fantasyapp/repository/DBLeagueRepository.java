package fantasyapp.repository;

import fantasyapp.dao.LeagueDAO;
import fantasyapp.repository.mapper.LeagueMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.repository.LeagueRepository;

import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class DBLeagueRepository implements LeagueRepository {

    private MongoCollection<LeagueDAO> leagueCollection;

    @Inject
    public DBLeagueRepository(MongoDatabase database) {
        leagueCollection = database.getCollection("leagues", LeagueDAO.class);
    }

    @Override
    public League getLeagueById(String leagueId) {
        LeagueDAO dao = leagueCollection.find(eq("_id", leagueId)).first();
        if (dao == null) return null;
        return LeagueMapper.daoToLeague(dao);
    }

    @Override
    public void setStatus(String leagueId, LeagueStatus status) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("status", status.toString()));
    }

    @Override
    public boolean isNameTaken(String name) {
        return null != leagueCollection.find(
                regex("name", "^(?i)" + Pattern.quote(name) + "$")).first();
    }

    @Override
    public void addLeague(League league) {
        leagueCollection.insertOne(LeagueMapper.writeDAO(league));
    }

}
