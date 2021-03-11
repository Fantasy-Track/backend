package usecase.meets;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.Meet;
import domain.exception.ApplicationException;
import domain.exception.LeagueNotExists;
import domain.exception.MeetNotExists;
import domain.exception.MeetNotScored;
import domain.exception.QuotaReachedMinutes;
import domain.exception.UnauthorizedException;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import domain.repository.QuotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.league.RemoteIndexer;

public class BackgroundMeetJobs {

    private Logger logger = LoggerFactory.getLogger(BackgroundMeetJobs.class);

    final int PULL_QUOTA_MINS = 60;
    final int RESCORE_QUOTA_MINS = 180;

    public MeetRepository meetRepository;
    public LeagueRepository leagueRepository;
    public RemoteIndexer remoteIndexer;
    public QuotaRepository quotaRepository;

    @Inject
    public BackgroundMeetJobs(MeetRepository meetRepository, LeagueRepository leagueRepository, RemoteIndexer remoteIndexer, QuotaRepository quotaRepository) {
        this.meetRepository = meetRepository;
        this.leagueRepository = leagueRepository;
        this.remoteIndexer = remoteIndexer;
        this.quotaRepository = quotaRepository;
    }

    public void pullMeets(String teamId, String leagueId) throws Exception {
        logger.info("Pulling meets for league: " + leagueId);

        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) throw new LeagueNotExists();
        else if (!league.owningTeam.equals(teamId)) throw new UnauthorizedException();
        else if (!quotaRepository.canPullMeets(leagueId)) throw new QuotaReachedMinutes(PULL_QUOTA_MINS);

        remoteIndexer.indexMeets(league.schoolId, leagueId, league.draftSettings.startTime);
        quotaRepository.triggerMeetsPulledQuota(leagueId, PULL_QUOTA_MINS);

        logger.info("Meets pulled successfully for: " + leagueId);
    }

    public void rescoreMeet(String meetId, String teamId, String leagueId) throws Exception {
        logger.info("Rescoring meet {} for league {}", meetId, leagueId);

        League league = leagueRepository.getLeagueById(leagueId);
        Meet meet = meetRepository.getMeetById(meetId);
        if (league == null) throw new LeagueNotExists();
        else if (meet == null) throw new MeetNotExists();
        else if (!meet.hasResults) throw new MeetNotScored();
        else if (!league.owningTeam.equals(teamId)) throw new UnauthorizedException();
        else if (!quotaRepository.canRescoreMeet(meetId)) throw new QuotaReachedMinutes(RESCORE_QUOTA_MINS);

        remoteIndexer.rescoreMeet(meetId);
        quotaRepository.triggerMeetRescoreQuota(meetId, RESCORE_QUOTA_MINS);

        logger.info("Meet {} marked for rescoring", meetId);
    }

}
