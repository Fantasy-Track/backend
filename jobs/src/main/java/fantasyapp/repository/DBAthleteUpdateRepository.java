package fantasyapp.repository;

import fantasyapp.dao.AthleteDAO;
import fantasyapp.repository.mapper.AthleteMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import domain.entity.Athlete;
import domain.repository.AthleteUpdateRepository;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DBAthleteUpdateRepository extends DBAthleteRepository implements AthleteUpdateRepository {

    private MongoCollection<AthleteDAO> athleteCollection;

    @Inject
    public DBAthleteUpdateRepository(MongoDatabase database) {
        super(database);
        athleteCollection = database.getCollection("athletes", AthleteDAO.class);
    }

    @Override
    public void setCategoryProjection(String athleteId, String categoryId, double projection) {
        athleteCollection.updateOne(eq("_id", athleteId), Updates.set("projections." + categoryId, projection));
    }

    @Override
    public void addAthlete(Athlete athlete) {
        athleteCollection.insertOne(AthleteMapper.entityToDAO(athlete));
    }

    @Override
    public void setPrimaryEvents(String id, List<String> eventIds) {
        athleteCollection.updateOne(eq("_id", id), Updates.set("primaryEvents", eventIds));
    }
}
