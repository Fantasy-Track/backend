package fantasyapp.repository;

import fantasyapp.dao.DraftDAO;
import fantasyapp.repository.mapper.DraftMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.entity.Draft;
import domain.repository.DraftRepository;

import static com.mongodb.client.model.Filters.eq;

public class DBDraftRepository implements DraftRepository {

    private MongoCollection<DraftDAO> draftCollection;

    @Inject
    public DBDraftRepository(MongoDatabase database) {
        draftCollection = database.getCollection("draft", DraftDAO.class);
    }

    @Override
    public Draft getDraftById(String id) {
        DraftDAO dao = draftCollection.find(eq("_id", id)).first();
        if (dao == null) return null;
        return DraftMapper.daoToDraft(dao);

    }

    @Override
    public void addDraft(Draft draft) {
        draftCollection.insertOne(DraftMapper.draftToDAO(draft));
    }

    @Override
    public void removeDraft(String id) {
        draftCollection.deleteOne(eq("_id", id));
    }

}
