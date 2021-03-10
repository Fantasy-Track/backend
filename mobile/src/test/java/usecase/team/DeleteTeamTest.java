package usecase.team;

import domain.entity.League;
import domain.exception.CannotDoActionNow;
import domain.exception.LeagueManagerCannotLeave;
import domain.exception.UnauthorizedException;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;
import mock.LeagueBank;
import mock.MockDistributedLock;
import mock.TeamBank;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import static org.mockito.Mockito.*;

public class DeleteTeamTest {

    private LeagueRepository leagueRepository;
    private TeamRepository teamRepository;
    private DeleteTeam deleteTeam;

    @BeforeMethod
    public void setUp() {
        leagueRepository = mock(LeagueRepository.class);
        teamRepository = mock(TeamRepository.class);
        NotificationFactory.handler = mock(NotificationHandler.class);

        when(teamRepository.getTeamById("T1")).then(invocation -> TeamBank.TEAM1);
        when(teamRepository.getTeamById("T2")).then(invocation -> TeamBank.TEAM2);
        when(teamRepository.getTeamById("T3")).then(invocation -> TeamBank.OTHER_LEAGUE_TEAM);
        deleteTeam = new DeleteTeam(teamRepository, leagueRepository, new MockDistributedLock());
    }

    @Test
    public void leagueStatusNotPreDraft() throws Exception{
        League league = LeagueBank.postDraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        try {
            deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                    .teamToDeleteId("T2")
                    .actingTeamId("T2")
                    .leagueId(league.id)
                    .build());
            Assert.fail();
        } catch (CannotDoActionNow ignored) { }
    }

    @Test
    public void teamIsLeagueManager() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        try {
            deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                    .teamToDeleteId("T1")
                    .actingTeamId("T1")
                    .leagueId("test")
                    .build());            Assert.fail();
        } catch (LeagueManagerCannotLeave ignored) { }
    }

    @Test
    public void teamDeleteUnAuth() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        try {
            deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                    .teamToDeleteId("T2")
                    .actingTeamId("T3")
                    .leagueId("test")
                    .build());
            Assert.fail();
        } catch (UnauthorizedException ignored) { }
    }

    @Test
    public void kickTeamSuccess() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                .teamToDeleteId("T2")
                .actingTeamId("T1")
                .leagueId("test")
                .build());

        verify(teamRepository).deleteTeam("T2");
    }

    @Test
    public void deleteTeamSuccess() throws Exception {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                .teamToDeleteId("T2")
                .actingTeamId("T2")
                .leagueId("test")
                .fcmToken("deviceToken")
                .build());

        verify(teamRepository).deleteTeam("T2");
        verify(NotificationFactory.handler).unregisterDeviceFromToken("deviceToken", "T2");
        verify(NotificationFactory.handler).unregisterDeviceFromToken("deviceToken", "test");
    }
}