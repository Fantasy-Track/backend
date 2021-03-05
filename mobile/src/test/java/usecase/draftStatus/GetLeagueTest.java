package usecase.draftStatus;

import domain.entity.Draft;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.LeagueNotExists;
import domain.repository.DraftRepository;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;
import mock.MockNameRepo;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.dto.LeagueDTO;
import usecase.league.GetLeague;

import java.time.Instant;

import static org.testng.Assert.*;

public class GetLeagueTest {

    DraftRepository draftRepository;
    LeagueRepository leagueRepository;
    TeamRepository teamRepository;
    GetLeague draftFetcher;

    @BeforeMethod
    public void setUp() {
        draftRepository = Mockito.mock(DraftRepository.class);
        leagueRepository = Mockito.mock(LeagueRepository.class);
        teamRepository = Mockito.mock(TeamRepository.class);
        draftFetcher = new GetLeague(draftRepository, teamRepository, leagueRepository, new MockNameRepo());

    }

    @Test
    public void testLeagueNotReal() {
        Mockito.when(draftRepository.getDraftById("test")).then(invocation -> null);
        Mockito.when(leagueRepository.getLeagueById("test")).then(invocation -> null);

        try {
            draftFetcher.getLeagueInfo("test");
            Assert.fail();
        } catch (LeagueNotExists ignored) { }
    }

    @Test
    public void testGetDraftRunning() throws LeagueNotExists {
        Mockito.when(draftRepository.getDraftById("test")).then(invocation -> Draft.builder()
                .id("test")
                .ipAddress("localhost:8080")
                .build());
        Mockito.when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .id("test")
                .status(LeagueStatus.DRAFTING)
                .draftSettings(League.DraftSettings.builder().startTime(Instant.now()).build())
                .build());

        LeagueDTO league = draftFetcher.getLeagueInfo("test");

        assertEquals(league.id, "test");
        assertEquals(league.status, LeagueStatus.DRAFTING);
        assertNotNull(league.draftIPAddress);
        assertNotNull(league.draftIPAddress);
        assertEquals(league.name, "test_name");
    }

    @Test
    public void testGetDraftNotRunning() throws LeagueNotExists {
        Mockito.when(teamRepository.countTeamsInLeague("test")).then(invocation -> 2);
        Mockito.when(draftRepository.getDraftById("test")).then(invocation -> null);
        Mockito.when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .id("test")
                .status(LeagueStatus.PRE_DRAFT)
                .maxTeams(4)
                .draftSettings(League.DraftSettings.builder().startTime(Instant.now()).build())
                .build());

        LeagueDTO league = draftFetcher.getLeagueInfo("test");

        assertEquals(league.id, "test");
        assertEquals(league.maxTeams, 4);
        assertEquals(league.numTeams, 2);
        assertEquals(league.status, LeagueStatus.PRE_DRAFT);
        assertNull(league.draftIPAddress);
        assertNotNull(league.draftTime);
        assertEquals(league.name, "test_name");
    }
}