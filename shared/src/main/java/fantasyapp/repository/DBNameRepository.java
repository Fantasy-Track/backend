package fantasyapp.repository;

import fantasyapp.dao.*;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.repository.NameRepository;

import static com.mongodb.client.model.Filters.eq;

public class DBNameRepository implements NameRepository {

    private MongoCollection<MeetDAO> meetCollection;
    private MongoCollection<AthleteDAO> athleteCollection;
    private MongoCollection<OwnerDAO> ownerCollection;
    private MongoCollection<LeagueDAO> leagueCollection;
    private MongoCollection<TeamDAO> teamCollection;

    @Inject
    public DBNameRepository(MongoDatabase database) {
        meetCollection = database.getCollection("meets", MeetDAO.class);
        athleteCollection = database.getCollection("athletes", AthleteDAO.class);
        ownerCollection = database.getCollection("owners", OwnerDAO.class);
        leagueCollection = database.getCollection("leagues", LeagueDAO.class);
        teamCollection = database.getCollection("teams", TeamDAO.class);
    }

    @Override
    public String getAthleteName(String athleteId) {
        AthleteDAO dao = athleteCollection.find(eq("_id", athleteId)).first();
        if (dao == null) return "";
        return dao.getName();
    }

    @Override
    public String getMeetName(String meetId) {
        MeetDAO dao = meetCollection.find(eq("_id", meetId)).first();
        if (dao == null) return "";
        return dao.getName();
    }

    @Override
    public String getOwnerName(String ownerId) {
        OwnerDAO dao = ownerCollection.find(eq("_id", ownerId)).first();
        if (dao == null) return  "";
        return dao.getName();
    }

    @Override
    public String getLeagueName(String leagueId) {
        LeagueDAO dao = leagueCollection.find(eq("_id", leagueId)).first();
        if (dao == null) return "";
        return dao.getName();
    }

    @Override
    public String getTeamName(String teamId) {
        TeamDAO dao = teamCollection.find(eq("_id", teamId)).first();
        if (dao == null) return "";
        return dao.getName();
    }

    @Override
    public void updateLeagueName(String leagueId, String leagueName) {
        leagueCollection.updateOne(eq("_id", leagueId), Updates.set("name", leagueName));
    }

    @Override
    public void updateTeamName(String teamId, String teamName) {
        teamCollection.updateOne(eq("_id", teamId), Updates.set("name", teamName));
    }

    @Override
    public void updateOwnerName(String ownerId, String ownerName) {
        ownerCollection.updateOne(eq("_id", ownerId), Updates.set("name", ownerName));
    }

}
