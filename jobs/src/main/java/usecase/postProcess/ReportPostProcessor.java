package usecase.postProcess;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.entity.ScoredResult;
import domain.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.fantasyPoints.PointsUpdater;
import usecase.notification.NotificationFactory;
import usecase.scoring.ResultsFilter;

import java.util.List;

public class ReportPostProcessor {

    private Logger logger = LoggerFactory.getLogger(ReportPostProcessor.class);

    private ResultsUploader resultsUploader;
    private PointsUpdater pointsUpdater;
    private MeetRepository meetRepository;
    private ResultsFilter resultsFilter;

    @Inject
    public ReportPostProcessor(ResultsUploader resultsUploader, PointsUpdater pointsUpdater, MeetRepository meetRepository, ResultsFilter resultsFilter) {
        this.resultsUploader = resultsUploader;
        this.pointsUpdater = pointsUpdater;
        this.meetRepository = meetRepository;
        this.resultsFilter = resultsFilter;
    }

    public void postProcess(List<ScoredResult> allResults, Meet meet) {
        logger.info(String.format("Post processing results (Meet: %s, League: %s, # Results: %d)", meet.athleticId, meet.leagueId, allResults.size()));
        List<ScoredResult> leagueResults = resultsFilter.filterLeagueAthletes(allResults, meet.leagueId);
        resultsUploader.uploadAllResults(leagueResults, meet);
        pointsUpdater.recalculatePointsForLeague(meet.leagueId);
        meetRepository.flagMeetAsHasResults(meet.id);
        logger.info("Finished processing meet, sending notification to users");
        NotificationFactory.handler.sendMeetProcessed(meet);
    }


}
