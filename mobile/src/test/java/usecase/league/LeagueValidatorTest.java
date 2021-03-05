package usecase.league;

import domain.exception.ApplicationException;
import domain.exception.SchoolNotEnoughAthletes;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.createTeam.TeamNameValidator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LeagueValidatorTest {

    private LeagueNameValidator nameValidator;
    private LeagueValidator leagueValidator;
    private DraftTimeValidator timeValidator;
    private TeamNameValidator teamNameValidator;

    @BeforeMethod
    public void setUp() {
        nameValidator = mock(LeagueNameValidator.class);
        timeValidator = mock(DraftTimeValidator.class);
        teamNameValidator = mock(TeamNameValidator.class);
        leagueValidator = new LeagueValidator(nameValidator, teamNameValidator, timeValidator);
    }

    @Test
    public void testDraftTimeOk() throws ApplicationException {
        CreateLeagueRequest request = CreateLeagueRequest.builder()
                .name("My League")
                .ownerId("O1")
                .teamName("T1")
                .draftTime(Instant.now().plus(11, ChronoUnit.MINUTES))
                .schoolId("904")
                .build();
        leagueValidator.validateLeagueSetup(request);
        verify(nameValidator).validateName(request.name);
        verify(teamNameValidator).validateNameIgnoreTaken(request.teamName);
        verify(timeValidator).validateDraftTime(request.draftTime);
    }

    @Test
    public void testNotEnoughAthletes() throws ApplicationException {
        try {
            leagueValidator.validateAthletesInLeague(List.of("A1", "A2"), 2);
            Assert.fail();
        } catch (SchoolNotEnoughAthletes ignored) {}
    }

    @Test
    public void testEnoughAthletes() throws ApplicationException {
        leagueValidator.validateAthletesInLeague(List.of("A1", "A2", "A3", "A4"), 2);
    }

}