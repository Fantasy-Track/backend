package fantasyapp.repository;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.repository.QuotaRepository;
import org.bson.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class DBQuotaRepository  implements QuotaRepository {

    private MongoCollection<Document> quotas;

    @Inject
    public DBQuotaRepository(MongoDatabase database) {
        this.quotas = database.getCollection("quotas");
    }


    @Override
    public void triggerMeetsPulledQuota(String leagueId, int minutes) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("_id", "MP_" + leagueId);
        map.put("expireAt", Instant.now().plus(minutes, ChronoUnit.MINUTES));
        quotas.insertOne(new Document(map));
    }

    @Override
    public boolean canPullMeets(String leagueId) {
        return quotas.find(eq("_id", "MP_" + leagueId)).first() == null;
    }

    @Override
    public void triggerAthletesPulledQuota(String leagueId, int minutes) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("_id", "AP_" + leagueId);
        map.put("expireAt", Instant.now().plus(minutes, ChronoUnit.MINUTES));
        quotas.insertOne(new Document(map));
    }

    @Override
    public boolean canPullAthletes(String leagueId) {
        return quotas.find(eq("_id", "AP_" + leagueId)).first() == null;
    }
}
