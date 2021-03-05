package usecase.meetProcessing;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.entity.EventResultsTable;
import domain.entity.Meet;
import domain.entity.ScoredResult;
import domain.exception.ResultsNotPosted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.pageExtraction.ResultsPageExtractor;

import java.util.LinkedList;
import java.util.List;

public class GenerateMeetReport {

    private Logger logger = LoggerFactory.getLogger(GenerateMeetReport.class);

    private ResultsPageExtractor pageExtractor;
    private GenerateEventReport generateEventReport;

    @Inject
    public GenerateMeetReport(ResultsPageExtractor pageExtractor, GenerateEventReport generateEventReport) {
        this.pageExtractor = pageExtractor;
        this.generateEventReport = generateEventReport;
    }

    public List<ScoredResult> generate(Meet meet) throws Exception {
        logger.info(String.format("Generating Meet Report (Meet: %s, League: %s)", meet.id, meet.leagueId));

        List<EventResultsTable> eventResultsTables = pageExtractor.extractEventSections(meet);
        if (eventResultsTables.size() == 0) throw new ResultsNotPosted();
        return culminateEventResults(eventResultsTables, meet);
    }

    private List<ScoredResult> culminateEventResults(List<EventResultsTable> tables, Meet meet) {
        List<ScoredResult> results = new LinkedList<>();
        for (EventResultsTable table : tables) {
            results.addAll(generateEventReport(table, meet));
        }
        return results;
    }

    private List<ScoredResult> generateEventReport(EventResultsTable table, Meet meet) {
        if (!Events.isValidEvent(table.getEventID())) return new LinkedList<>();
        return generateEventReport.generate(table, meet);
    }

}
