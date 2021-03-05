package fantasyapp.repository;

import fantasyapp.dao.ContractDAO;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.repository.RosterRepository;

import java.util.Set;

import static com.mongodb.client.model.Filters.*;

public class DBRosterRepository implements RosterRepository {

    private MongoCollection<ContractDAO> contractCollection;

    @Inject
    public DBRosterRepository(MongoDatabase database) {
        contractCollection = database.getCollection("contracts", ContractDAO.class);
    }

    @Override
    public boolean isAthleteOnRoster(String athleteId, String teamId) {
        ContractDAO contract = contractCollection.find(and(
                eq("athleteId", athleteId),
                eq("teamId", teamId))).first();
        return contract != null;
    }

    @Override
    public int countAthletesInEventCategory(String eventCategoryId, String teamId) {
        Set<String> eventIds = Events.getEventsInCategory(eventCategoryId);
        return (int) contractCollection.countDocuments(and(
                eq("teamId", teamId),
                in("eventId", eventIds)
        ));
    }

    @Override
    public void setAthleteEvent(String athleteId, String eventId, String teamId) {
        contractCollection.updateOne(and(
                eq("athleteId", athleteId),
                eq("teamId", teamId)), Updates.set("eventId", eventId));
    }
}
