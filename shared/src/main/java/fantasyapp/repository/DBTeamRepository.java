package fantasyapp.repository;

import fantasyapp.dao.TeamDAO;
import fantasyapp.repository.mapper.TeamMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.entity.Team;
import domain.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DBTeamRepository implements TeamRepository {

    private MongoCollection<TeamDAO> teamCollection;

    @Inject
    public DBTeamRepository(MongoDatabase database) {
        teamCollection = database.getCollection("teams", TeamDAO.class);
    }

    @Override
    public boolean isNameTaken(String name, String leagueId) {
        return null != teamCollection.find(and(
                eq("name", name), eq("leagueId", leagueId))).first();
    }

    @Override
    public boolean doesOwnerHaveTeamInLeague(String ownerId, String leagueId) {
        return null != teamCollection.find(and(eq("ownerId", ownerId), eq("leagueId", leagueId))).first();
    }

    @Override
    public Team getTeamById(String teamId) {
        TeamDAO dao = teamCollection.find(eq("_id", teamId)).first();
        if (dao == null) return null;
        return TeamMapper.daoToTeam(dao);
    }

    @Override
    public void addTeam(Team team) {
        teamCollection.insertOne(TeamMapper.teamToDAO(team));
    }

    @Override
    public void deleteTeam(String teamId) {
        teamCollection.deleteOne(eq("_id", teamId));
    }

    @Override
    public void updateFantasyPoints(String teamId, double points) {
        teamCollection.updateOne(eq("_id", teamId), Updates.set("fantasyPoints", points));
    }

    @Override
    public List<Team> getOwnedTeams(String ownerId) {
        Iterable<TeamDAO> daos = teamCollection.find(eq("ownerId", ownerId));
        List<Team> teams = new ArrayList<>();
        for (TeamDAO dao : daos) {
            teams.add(TeamMapper.daoToTeam(dao));
        }
        return teams;
    }

    @Override
    public List<Team> getTeamsInLeague(String leagueId) {
        Iterable<TeamDAO> daos = teamCollection.find(eq("leagueId", leagueId));
        List<Team> teams = new ArrayList<>();
        for (TeamDAO dao : daos) {
            teams.add(TeamMapper.daoToTeam(dao));
        }
        return teams;
    }

    @Override
    public int countTeamsInLeague(String leagueId) {
        return (int) teamCollection.countDocuments(eq("leagueId", leagueId));
    }

}
