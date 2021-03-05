package usecase.league;

import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.repository.LeagueRepository;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.createTeam.CreateTeam;
import usecase.createTeam.CreateTeamRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateLeagueTest {

    private LeagueValidator validator;
    private RemoteIndexer remoteIndexer;
    private LeagueRepository leagueRepository;
    private CreateLeague leagueCreator;
    private CreateTeam createTeam;

    @BeforeMethod
    public void setUp() throws Exception {
        validator = mock(LeagueValidator.class);
        remoteIndexer = mock(RemoteIndexer.class);
        leagueRepository = mock(LeagueRepository.class);
        createTeam = mock(CreateTeam.class);
        when(remoteIndexer.indexAthletesInSchool("904")).then(invocation -> List.of("A1", "A2"));

        leagueCreator = new CreateLeague(validator, remoteIndexer, leagueRepository, createTeam);
    }

    @Test
    public void testCreateLeague() throws Exception {
        CreateLeagueRequest request = CreateLeagueRequest.builder()
                .name("My League  ")
                .schoolId("904")
                .draftTime(Instant.now().plus(15, ChronoUnit.MINUTES))
                .teamName("Team Name")
                .ownerId("Owner Name")
                .build();

        CreateLeagueDTO dto = leagueCreator.createLeague(request);
        verify(validator).validateLeagueSetup(request);
        verify(remoteIndexer).indexAthletesInSchool("904");
        verify(remoteIndexer).indexMeets(eq("904"), any(String.class), any(Instant.class));

        ArgumentCaptor<CreateTeamRequest> requestCaptor = ArgumentCaptor.forClass(CreateTeamRequest.class);
        verify(createTeam).createInNonExistingLeague(requestCaptor.capture());
        assertEquals(requestCaptor.getValue().teamName, "Team Name");
        assertNotNull(requestCaptor.getValue().leagueId);
        assertEquals(requestCaptor.getValue().ownerId, "Owner Name");

        ArgumentCaptor<League> captor = ArgumentCaptor.forClass(League.class);
        verify(leagueRepository).addLeague(captor.capture());

        League league = captor.getValue();
        assertEquals(league.athletes, List.of("A1", "A2"));
        assertEquals(league.status, LeagueStatus.PRE_DRAFT);
        assertEquals(league.id.length(), 6);
        assertEquals(dto.owningTeamId, league.owningTeam);
        assertEquals(dto.leagueId, league.id);
        assertNotNull(league.draftSettings);
        assertNotNull(league.leagueSettings);
        assertEquals(league.name, "My League");
    }
}