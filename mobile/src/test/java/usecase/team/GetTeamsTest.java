package usecase.team;

import domain.entity.Team;
import domain.exception.TeamNotExists;
import domain.repository.TeamRepository;
import mock.MockNameRepo;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GetTeamsTest {

    GetTeams ownedTeams;
    TeamRepository teamRepository;

    Team t1 = Team.builder().id("T1").ownerId("owner").name("ownerName").leagueId("test").build();
    Team t2 = Team.builder().id("T2").ownerId("owner").name("ownerName").leagueId("testTwo").build();
    Team t3 = Team.builder().id("T3").ownerId("notOwner").name("notOwnerName").leagueId("test").build();

    @BeforeMethod
    public void setUp() {
        teamRepository = mock(TeamRepository.class);

        when(teamRepository.getOwnedTeams("owner")).then(invocation -> List.of(t1, t2));
        when(teamRepository.getTeamsInLeague("test")).then(invocation -> List.of(t1, t3));

        ownedTeams = new GetTeams(teamRepository, new MockNameRepo());
    }

    @Test
    public void testNoTeamsOwned() {
        List<TeamDTO> dtos = ownedTeams.getTeamsOwnedBy("imaginaryOwner");

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void testTeamsOwnedBy() {
        List<TeamDTO> dtos = ownedTeams.getTeamsOwnedBy("owner");

        assertEquals(dtos.size(), 2);
        assertTrue(dtos.contains(TeamDTO.builder().id("T1").build()));
        assertTrue(dtos.contains(TeamDTO.builder().id("T2").build()));
    }

    @Test
    public void testGetTeamsInLeague() {
        List<TeamDTO> dtos = ownedTeams.getTeamsInLeague("test");

        assertTrue(dtos.contains(TeamDTO.builder().id("T1").build()));
        assertTrue(dtos.contains(TeamDTO.builder().id("T3").build()));
    }

    @Test
    public void testGetTeamByIdNull() {
        when(teamRepository.getTeamById("T1")).then(invocation -> null);

        try {
            ownedTeams.getTeamById("T1");
            Assert.fail();
        } catch (TeamNotExists ignored) { }
    }

    @Test
    public void testGetTeamByNotNull() throws TeamNotExists {
        when(teamRepository.getTeamById("T1")).then(invocation -> t1);

        TeamDTO dto = ownedTeams.getTeamById("T1");
        assertEquals(dto.id, "T1");
    }
}