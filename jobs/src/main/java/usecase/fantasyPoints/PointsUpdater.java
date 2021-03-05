package usecase.fantasyPoints;

import com.google.inject.Inject;
import domain.entity.Team;
import domain.repository.MeetRepository;
import domain.repository.ResultRepository;
import domain.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PointsUpdater {

    private final Logger logger = LoggerFactory.getLogger(PointsUpdater.class);

    private ResultRepository resultRepository;
    private TeamRepository teamRepository;
    private MeetRepository meetRepository;

    @Inject
    public PointsUpdater(ResultRepository resultRepository, TeamRepository teamRepository, MeetRepository meetRepository) {
        this.resultRepository = resultRepository;
        this.teamRepository = teamRepository;
        this.meetRepository = meetRepository;
    }

    public void recalculatePointsForLeague(String leagueId) {
        logger.info("Updating team points");
        List<String> meetIds = meetRepository.getEnabledMeetsInLeague(leagueId).stream().map(meet -> meet.id).collect(Collectors.toList());
        for (Team team : teamRepository.getTeamsInLeague(leagueId)) {
            double points = resultRepository.aggregatePointsAtMeets(team.id, meetIds);
            teamRepository.updateFantasyPoints(team.id, points);
            logger.info(String.format("Set team points (teamId=%s, points=%f)", team.id, points));
        }
    }

}
