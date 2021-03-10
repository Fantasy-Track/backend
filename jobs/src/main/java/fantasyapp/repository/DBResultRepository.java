package fantasyapp.repository;

import fantasyapp.dao.ResultDAO;
import fantasyapp.repository.mapper.ResultMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.ReplaceOptions;
import domain.entity.ScoredResult;
import domain.repository.ResultRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DBResultRepository implements ResultRepository {

    private Logger logger = LoggerFactory.getLogger(DBResultRepository.class);
    private MongoCollection<ResultDAO> resultCollection;

    @Inject
    public DBResultRepository(MongoDatabase database) {
        resultCollection = database.getCollection("results", ResultDAO.class);
    }

    @Override
    public void addResult(ScoredResult result, String teamId) {
        ResultDAO dao = ResultMapper.resultToDAO(result, teamId);
        resultCollection.replaceOne(eq("_id", dao.getId()), dao, new ReplaceOptions().upsert(true));
    }

    @Override
    public void addResult(ScoredResult result) {
        addResult(result, null);
    }

    @Override
    public double aggregatePointsAtMeets(String teamId, List<String> meetIds) {
        Document document = resultCollection.withDocumentClass(Document.class).aggregate(Arrays.asList(
                Aggregates.match(and(eq("teamId", teamId), in("meetId", meetIds))),
                Aggregates.group("$teamId", Accumulators.sum("fantasyPoints", "$fantasyPoints"))
        )).first();
        if (document == null) return 0;
        return document.getDouble("fantasyPoints");
    }

    @Override
    public void removeMeetResults(String meetId) {
        var response = resultCollection.deleteMany(eq("meetId", meetId));
        logger.info("Deleted {} results for Meet ID: {}", response.getDeletedCount(), meetId);
    }

}
