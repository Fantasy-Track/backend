package usecase.league;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.CannotDoActionNow;
import domain.exception.LeagueNotExists;
import domain.exception.QuotaReachedMinutes;
import domain.exception.UnauthorizedException;
import domain.repository.EditLeagueRepository;
import domain.repository.QuotaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PullAthletes {

    public static final int QUOTA_MINS = 60;

    private EditLeagueRepository leagueRepository;
    private QuotaRepository quotaRepository;
    private RemoteIndexer remoteIndexer;

    @Inject
    public PullAthletes(EditLeagueRepository leagueRepository, QuotaRepository quotaRepository, RemoteIndexer remoteIndexer) {
        this.leagueRepository = leagueRepository;
        this.quotaRepository = quotaRepository;
        this.remoteIndexer = remoteIndexer;
    }

    public void pullAthletesForLeague(String teamId, String leagueId) throws Exception {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) throw new LeagueNotExists();
        else if (league.status != LeagueStatus.PRE_DRAFT) throw new CannotDoActionNow();
        else if (!teamId.equals(league.owningTeam)) throw new UnauthorizedException();
        else if(!quotaRepository.canPullAthletes(leagueId)) throw new QuotaReachedMinutes(QUOTA_MINS);

        List<String> athletes = remoteIndexer.indexAthletesInSchool(league.schoolId);
        leagueRepository.setAthletes(leagueId, athletes);

        quotaRepository.triggerAthletesPulledQuota(leagueId, QUOTA_MINS);
    }

}
