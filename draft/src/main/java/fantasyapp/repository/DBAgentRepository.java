package fantasyapp.repository;

import fantasyapp.dao.AthleteDAO;
import fantasyapp.dao.ContractDAO;
import fantasyapp.dao.LeagueDAO;
import fantasyapp.draftInstance.LeagueId;
import fantasyapp.repository.mapper.AthleteMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Sorts;
import domain.entity.Athlete;
import domain.repository.AgentRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DBAgentRepository implements AgentRepository {

    private String leagueId;

    private MongoCollection<LeagueDAO> leagues;
    private MongoCollection<AthleteDAO> athletes;
    private MongoCollection<ContractDAO> contracts;

    @Inject
    public DBAgentRepository(MongoDatabase database, @LeagueId String leagueId) {
        this.leagueId = leagueId;
        this.leagues = database.getCollection("leagues", LeagueDAO.class);
        this.contracts = database.getCollection("contracts", ContractDAO.class);
        this.athletes = database.getCollection("athletes", AthleteDAO.class);
    }

    @Override
    public List<Athlete> getAllFreeAgents() {
        LeagueDAO league = leagues.find(eq("_id", leagueId)).first();

        Iterable<String> draftedAgents = contracts.distinct("athleteId", eq("leagueId", leagueId), String.class);
        Iterable<AthleteDAO> freeAthletes = athletes.find(and(nin("_id", draftedAgents), in("_id", league.getAthletes())));

        List<Athlete> freeAgents = new ArrayList<>();
        freeAthletes.forEach(athleteDAO -> freeAgents.add(AthleteMapper.daoToAthlete(athleteDAO)));
        return freeAgents;
    }

    @Override
    public Athlete getFreeAgentWithHighestProjection() {
        LeagueDAO league = leagues.find(eq("_id", leagueId)).first();

        Iterable<String> draftedAgents = contracts.distinct("athleteId", eq("leagueId", leagueId), String.class);
        AthleteDAO dao = athletes.aggregate(List.of(
                Aggregates.match(and(nin("_id", draftedAgents), in("_id", league.getAthletes()))),
                Aggregates.addFields(new Field("maxProjection", Accumulators.max("maxProjection", "$projection"))),
                Aggregates.sort(Sorts.descending("maxProjection"))
        )).first();
        return dao == null ? null : AthleteMapper.daoToAthlete(dao);
    }
}
