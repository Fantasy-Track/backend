package usecase.createTeam;

import domain.entity.Team;
import domain.exception.ApplicationException;
import domain.repository.TeamRepository;
import org.mockito.ArgumentCaptor;
import mock.MockDistributedLock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateTeamTest {

    private TeamNameValidator nameValidator;
    private JoinLeagueValidator joinValidator;
    private TeamRepository teamRepository;

    private CreateTeam createTeam;

    @BeforeMethod
    public void setUp() {
        nameValidator = mock(TeamNameValidator.class);
        joinValidator = mock(JoinLeagueValidator.class);
        teamRepository = mock(TeamRepository.class);
        createTeam = new CreateTeam(nameValidator, joinValidator, teamRepository, new MockDistributedLock());
    }

    @Test
    public void testCreateTeam() throws ApplicationException {
        CreateTeamRequest request = CreateTeamRequest.builder()
                .ownerId("owner")
                .teamName("teamName   ") // keep the spaces
                .leagueId("test")
                .build();
        String teamId = createTeam.createTeam(request);

        verify(nameValidator).validateName(request.teamName, request.leagueId);
        verify(joinValidator).validateOwnerJoiningLeague(request.ownerId, request.leagueId);

        ArgumentCaptor<Team> captor = ArgumentCaptor.forClass(Team.class);
        verify(teamRepository).addTeam(captor.capture());

        Team team = captor.getValue();
        assertNotNull(team);
        assertEquals(team.id, teamId);
        assertEquals(team.name, "teamName");
        assertEquals(team.ownerId, "owner");
        assertEquals(team.leagueId, "test");
    }
}