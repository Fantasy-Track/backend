package usecase.meetProcessing;

import com.google.inject.Inject;
import domain.entity.EventResultsTable;
import domain.entity.IndexedResult;
import domain.entity.Meet;
import domain.entity.ScoredResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.indexing.EventResultsIndexer;
import usecase.scoring.ResultsFilter;
import usecase.scoring.ResultsScorer;

import java.util.List;

public class GenerateEventReport {

    private final Logger logger = LoggerFactory.getLogger(GenerateEventReport.class);

    private ResultsScorer scorer;
    private ResultsFilter filter;

    @Inject
    public GenerateEventReport(ResultsScorer scorer, ResultsFilter filter) {
        this.scorer = scorer;
        this.filter = filter;
    }

    public List<ScoredResult> generate(EventResultsTable eventResultsTable, Meet meet) {
        logger.debug(String.format("Generating Event Report (Meet: %s, League: %s, Event: %s)", meet.athleticId, meet.leagueId, eventResultsTable.getEventID()));

        List<IndexedResult> indexedResults = indexResults(eventResultsTable, meet);
        return scoreIndexedResults(indexedResults, meet.leagueId);
    }

    private List<IndexedResult> indexResults(EventResultsTable eventResultsTable, Meet meet) {
        EventResultsIndexer indexer = new EventResultsIndexer(eventResultsTable, meet);
        return indexer.indexEventResults();
    }

    private List<ScoredResult> scoreIndexedResults(List<IndexedResult> indexedResults, String leagueId) {
        indexedResults = filter.filterInvalid(indexedResults);
        return scorer.scoreResultsForAthletesInLeague(indexedResults, leagueId);
    }

}
