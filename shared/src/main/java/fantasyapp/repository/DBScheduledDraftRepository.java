package fantasyapp.repository;

import fantasyapp.dao.ScheduledDraftDAO;
import fantasyapp.repository.mapper.ScheduledDraftMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import domain.entity.ScheduledDraft;
import domain.repository.ScheduledDraftRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("NullableProblems")
public class DBScheduledDraftRepository implements ScheduledDraftRepository {

    private MongoCollection<ScheduledDraftDAO> draftCollection;

    @Inject
    public DBScheduledDraftRepository(MongoDatabase database) {
        draftCollection = database.getCollection("scheduled_drafts", ScheduledDraftDAO.class);
    }

    @Override
    public List<ScheduledDraft> getDraftsBefore(Instant time) {
        return draftCollection.find(Filters.lte("time", time))
                .map(ScheduledDraftMapper::writeEntity)
                .into(new ArrayList<>());
    }

    @Override
    public void deleteScheduledDraft(String id) {
        draftCollection.deleteOne(eq("_id", id));
    }

    @Override
    public ScheduledDraft getDraftForLeague(String leagueId) {
        ScheduledDraftDAO dao = draftCollection.find(Filters.eq("_id", leagueId)).first();
        return dao == null ? null : ScheduledDraftMapper.writeEntity(dao);
    }
}
