package fantasyapp.repository;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import domain.repository.StatsRepository;
import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

public class DBStatsRepository implements StatsRepository {

    private MongoCollection<Document> statsCollection;

    @Inject
    public DBStatsRepository(MongoDatabase database) {
        statsCollection = database.getCollection("teamMeetScores", Document.class);
    }

    @Override
    public double getTotalTeamPoints(String teamId) {
        Document doc = statsCollection.aggregate(Arrays.asList(
                Aggregates.match(eq("teamId", teamId)),
                Aggregates.group("$teamId", Accumulators.sum("points", "$points"))
        )).first();
        return doc == null ? 0 : doc.getDouble("points");
    }

}
