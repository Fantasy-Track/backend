package fantasyapp.repository;

import fantasyapp.dao.TradeDAO;
import fantasyapp.repository.mapper.TradeMapper;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.repository.TradeRepository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DBTradeRepository implements TradeRepository {

    private MongoCollection<TradeDAO> tradeCollection;

    @Inject
    public DBTradeRepository(MongoDatabase database) {
        tradeCollection = database.getCollection("trades", TradeDAO.class);
    }

    @Override
    public void addTradeProposal(TradeProposal proposal) {
        tradeCollection.insertOne(TradeMapper.writeDAO(proposal));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<TradeProposal> getTradesWithTeam(String teamId) {
        FindIterable<TradeDAO> daos = tradeCollection.find(or(eq("proposingTeamId", teamId), eq("acceptingTeamId", teamId)));
        return daos.map(TradeMapper::writeEntity).into(new ArrayList<>());
    }

    @Override
    public TradeProposal getTradeById(String id) {
        TradeDAO dao  = tradeCollection.find(eq("_id", id)).first();
        return dao == null ? null : TradeMapper.writeEntity(dao);
    }

    @Override
    public void setTradeStatus(String id, TradeStatus status) {
        tradeCollection.updateOne(eq("_id", id), Updates.set("status", status.toString())); // have to convert to string to parse
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<TradeProposal> getTradesWithStatus(TradeStatus status) {
        FindIterable<TradeDAO> daos = tradeCollection.find(eq("status", status.toString()));
        return daos.map(TradeMapper::writeEntity).into(new ArrayList<>());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<TradeProposal> getIncompleteTradesInLeague(String leagueId) {
        FindIterable<TradeDAO> daos = tradeCollection.find(and(eq("leagueId", leagueId), or(eq("status", TradeStatus.PENDING.toString()), eq("status", TradeStatus.ACCEPTED_WAITING.toString()))));
        return daos.map(TradeMapper::writeEntity).into(new ArrayList<>());
    }

    @Override
    public List<TradeProposal> getAcceptedTradesInLeague(String leagueId) {
        FindIterable<TradeDAO> daos = tradeCollection.find(and(eq("leagueId", leagueId), or(eq("status", TradeStatus.ACCEPTED_WAITING.toString()), eq("status", TradeStatus.ACCEPTED_ENACTED.toString()))));
        return daos.map(TradeMapper::writeEntity).into(new ArrayList<>());
    }

}
