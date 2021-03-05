package usecase.meets;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.Meet;
import domain.exception.MeetBeforeDraft;
import domain.exception.MeetNotExists;
import domain.exception.UnauthorizedException;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.league.LeaguePointsUpdater;
import util.TimeUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditMeets {

    private Logger logger = LoggerFactory.getLogger(EditMeets.class);

    private MeetRepository meetRepository;
    private LeagueRepository leagueRepository;
    private LeaguePointsUpdater pointsUpdater;

    @Inject
    public EditMeets(MeetRepository meetRepository, LeagueRepository leagueRepository, LeaguePointsUpdater pointsUpdater) {
        this.meetRepository = meetRepository;
        this.leagueRepository = leagueRepository;
        this.pointsUpdater = pointsUpdater;
    }

    public void editEnabledMeets(EditMeetsRequest request) throws Exception {
        League league = leagueRepository.getLeagueById(request.leagueId);
        if (!league.owningTeam.equals(request.teamId)) throw new UnauthorizedException();

        List<Meet> meets = meetRepository.getMeetsInLeague(request.leagueId);
        Map<String, Meet> meetIdMap = meets.stream().collect(Collectors.toMap(meet -> meet.id, meet -> meet));

        try {
            enableOrDisableMeets(meetIdMap, request.enableMeetIds, league, true);
            enableOrDisableMeets(meetIdMap, request.disableMeetIds, league, false);

            logger.info("Updating points for league: " + request.leagueId);
            pointsUpdater.updatePoints(request.leagueId);
        } catch (Exception e) {
            logger.error("Error updating points for league after editing meet... undoing operations", e);
            enableOrDisableMeets(meetIdMap, meets.stream().filter(m -> m.enabled).map(m -> m.id).collect(Collectors.toList()), league, true);
            enableOrDisableMeets(meetIdMap, meets.stream().filter(m -> !m.enabled).map(m -> m.id).collect(Collectors.toList()), league, false);
            throw e;
        }
    }

    private void editMeet(Meet meet, League league, boolean enable) throws MeetBeforeDraft, MeetNotExists {
        if (meet == null) throw new MeetNotExists();
        else if (enable && TimeUtil.isAfterPST(league.draftSettings.startTime, meet.date)) throw new MeetBeforeDraft();
        meetRepository.setMeetEnabled(meet.id, enable);
    }

    private void enableOrDisableMeets(Map<String, Meet> leagueMeetMap, List<String> meets, League league, boolean enable) throws MeetBeforeDraft, MeetNotExists {
        for (String meetId : meets) {
            Meet meet = leagueMeetMap.get(meetId);
            editMeet(meet, league, enable);
        }
    }

}
