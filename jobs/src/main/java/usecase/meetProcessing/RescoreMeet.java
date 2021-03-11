package usecase.meetProcessing;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.exception.MeetNotScored;
import domain.repository.MeetRepository;
import domain.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.fantasyPoints.PointsUpdater;

public class RescoreMeet {

    private Logger logger = LoggerFactory.getLogger(RescoreMeet.class);

    private MeetRepository meetRepository;
    private ResultRepository resultRepository;
    private PointsUpdater pointsUpdater;

    @Inject
    public RescoreMeet(MeetRepository meetRepository, ResultRepository resultRepository, PointsUpdater pointsUpdater) {
        this.meetRepository = meetRepository;
        this.resultRepository = resultRepository;
        this.pointsUpdater = pointsUpdater;
    }

    // removes the meet results so that it will be rescored with the next batch
    public void rescoreMeet(String meetId) {
        Meet meet = meetRepository.getMeetById(meetId);
        logger.info(String.format("Removing meet results: (Meet ID: %s, League ID: %s)", meetId, meet.leagueId));
        resultRepository.removeMeetResults(meetId);
        meetRepository.setRescoreMeet(meetId, true);
        meetRepository.setMeetHasResults(meetId, false);
        pointsUpdater.recalculatePointsForLeague(meet.leagueId);
    }

}
