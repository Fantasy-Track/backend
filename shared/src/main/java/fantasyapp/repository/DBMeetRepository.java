package fantasyapp.repository;

import fantasyapp.dao.MeetDAO;
import fantasyapp.repository.mapper.MeetMapper;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import domain.entity.Contract;
import domain.entity.Meet;
import domain.repository.MeetRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@SuppressWarnings("NullableProblems")
public class DBMeetRepository implements MeetRepository {

    private MongoCollection<MeetDAO> meetCollection;

    @Inject
    public DBMeetRepository(MongoDatabase database) {
        meetCollection = database.getCollection("meets", MeetDAO.class);
    }

    @Override
    public List<Meet> getMeetsInLeague(String leagueId) {
        FindIterable<MeetDAO> daos = meetCollection.find(eq("leagueId", leagueId));
        return daos.map(MeetMapper::daoToMeet).into(new ArrayList<>());
    }

    @Override
    public List<Meet> getAllLockedMeetsBetween(Instant start, Instant end) {
        FindIterable<MeetDAO> daos = meetCollection.find(
                and(eq("hasResults", false),
                        eq("locked", true),
                        gte("date", start),
                        lt("date", end)));
        return daos.map(MeetMapper::daoToMeet).into(new ArrayList<>());
    }

    @Override
    public void setMeetHasResults(String meetId, boolean hasResults) {
        meetCollection.updateOne(eq("_id", meetId), Updates.set("hasResults", hasResults));
    }

    @Override
    public void addOrReplaceMeet(Meet meet) {
        meetCollection.replaceOne(and(eq("athleticId", meet.athleticId), eq("leagueId", meet.leagueId)), MeetMapper.writeDAO(meet), new ReplaceOptions().upsert(true));
    }

    @Override
    public List<Meet> getEnabledUnlockedMeetsBetween(Instant start, Instant end) { //inclusive, exclusive
        return meetCollection.find(and(
                gte("date", start),
                lt("date", end),
                eq("locked", false),
                eq("enabled", true)))
                .map(MeetMapper::daoToMeet)
                .into(new ArrayList<>());
    }

    @Override
    public void saveContractsForMeet(String meetId, List<Contract> contracts) {
        meetCollection.updateOne(eq("_id", meetId), Updates.set("savedContracts", contracts));
    }

    @Override
    public void setMeetEnabled(String meetId, boolean enabled) {
        meetCollection.updateOne(eq("_id", meetId), Updates.set("enabled", enabled));
    }

    @Override
    public List<Meet> getEnabledMeetsInLeague(String leagueId) {
        return meetCollection.find(and(eq("leagueId", leagueId), eq("enabled", true)))
                .map(MeetMapper::daoToMeet).into(new ArrayList<>());
    }

    @Override
    public Meet getMeetById(String meetId) {
        MeetDAO dao = meetCollection.find(eq("_id", meetId)).first();
        return dao == null ? null : MeetMapper.daoToMeet(dao);
    }

    @Override
    public void flagMeetAsLocked(String meetId) {
        meetCollection.updateOne(eq("_id", meetId), Updates.set("locked", true));
    }
}
