package usecase.leagueCreation;

import domain.entity.League;
import domain.exception.CannotDoActionNow;
import domain.exception.QuotaReachedMinutes;
import domain.exception.UnauthorizedException;
import domain.repository.EditLeagueRepository;
import domain.repository.QuotaRepository;
import mock.LeagueBank;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.league.PullAthletes;
import usecase.league.RemoteIndexer;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PullAthletesTest {

    @Mock private EditLeagueRepository leagueRepository;
    @Mock private RemoteIndexer remoteIndexer;
    @Mock private QuotaRepository quotaRepository;
    private PullAthletes pullAthletes;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pullAthletes = new PullAthletes(leagueRepository, quotaRepository, remoteIndexer);
    }

    @Test
    public void testNotPreDraft() throws Exception {
        League league = LeagueBank.postDraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        try {
            pullAthletes.pullAthletesForLeague(league.owningTeam, league.id);
            Assert.fail();
        } catch (CannotDoActionNow ignored) {
        }
    }

    @Test
    public void testNotOwner() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        try {
            pullAthletes.pullAthletesForLeague("notTheManager", league.id);
            Assert.fail();
        } catch (UnauthorizedException ignored) {
        }
    }

    @Test
    public void testQuotaReached() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(quotaRepository.canPullAthletes(league.id)).then(invocation -> false);

        try {
            pullAthletes.pullAthletesForLeague("T1", league.id);
            Assert.fail();
        } catch (QuotaReachedMinutes ignored) {}
    }


    @Test
    public void testSuccess() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(remoteIndexer.indexAthletesInSchool(league.schoolId)).then(invocation -> List.of("A1", "A6", "A7"));
        when(quotaRepository.canPullAthletes(league.id)).then(invocation -> true);

        pullAthletes.pullAthletesForLeague("T1", league.id);

        verify(leagueRepository).setAthletes(league.id, List.of("A1", "A6", "A7"));
        verify(quotaRepository).triggerAthletesPulledQuota(league.id, PullAthletes.QUOTA_MINS);
    }

}
