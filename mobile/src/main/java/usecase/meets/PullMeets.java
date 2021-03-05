package usecase.meets;

import com.google.inject.Inject;
import domain.entity.League;
import domain.exception.LeagueNotExists;
import domain.exception.QuotaReachedMinutes;
import domain.exception.UnauthorizedException;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import domain.repository.QuotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.league.RemoteIndexer;

public class PullMeets {

    private Logger logger = LoggerFactory.getLogger(PullMeets.class);

    final int QUOTA_MINS = 60;

    public MeetRepository meetRepository;
    public LeagueRepository leagueRepository;
    public RemoteIndexer remoteIndexer;
    public QuotaRepository quotaRepository;

    @Inject
    public PullMeets(MeetRepository meetRepository, LeagueRepository leagueRepository, RemoteIndexer remoteIndexer, QuotaRepository quotaRepository) {
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
        else if (!quotaRepository.canPullMeets(leagueId)) throw new QuotaReachedMinutes(QUOTA_MINS);

        remoteIndexer.indexMeets(league.schoolId, leagueId, league.draftSettings.startTime);
        quotaRepository.triggerMeetsPulledQuota(leagueId, QUOTA_MINS);

        logger.info("Meets pulled successfully for: " + leagueId);
    }

}
