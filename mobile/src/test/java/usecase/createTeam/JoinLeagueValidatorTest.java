package usecase.createTeam;

import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.*;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JoinLeagueValidatorTest {

    private JoinLeagueValidator validator;
    private LeagueRepository leagueRepository;
    private TeamRepository teamRepository;

    @BeforeMethod
    public void setUp() {
        leagueRepository = mock(LeagueRepository.class);
        teamRepository = mock(TeamRepository.class);

        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .id("test")
                .maxTeams(3)
                .status(LeagueStatus.PRE_DRAFT)
                .athletes(List.of("A1", "A2"))
                .build());
        when(leagueRepository.getLeagueById("draftedLeague")).then(invocation -> League.builder()
                .id("notAcceptingLeague")
                .maxTeams(3)
                .status(LeagueStatus.DRAFTING)
                .athletes(List.of("A2", "A3"))
                .build());

        validator = new JoinLeagueValidator(teamRepository, leagueRepository);
    }

    @Test
    public void testCanJoinLeague() {
        try {
            validator.validateOwnerJoiningLeague("owner", "test");
        } catch (ApplicationException ignored) {
            Assert.fail();
        }
    }

    @Test
    public void testLeagueFull() throws ApplicationException {
        when(teamRepository.countTeamsInLeague("test")).then(invocation -> 3);
        try {
            validator.validateOwnerJoiningLeague("owner", "test");
            Assert.fail();
        } catch (LeagueFull ignored) { }
    }

    @Test
    public void testLeagueNotAccepting() throws ApplicationException {
        try {
            validator.validateOwnerJoiningLeague("owner", "draftedLeague");
            Assert.fail();
        } catch (AlreadyDrafted ignored) { }
    }

    @Test
    public void testLeagueDoesntExist() throws ApplicationException {
        try {
            validator.validateOwnerJoiningLeague("owner", "imaginaryLeague");
            Assert.fail();
        } catch (LeagueNotExists ignored) { }
    }

    @Test
    public void testAlreadyInLeague() throws ApplicationException {
        when(teamRepository.doesOwnerHaveTeamInLeague("ownerInLeague", "test")).then(invocation -> true);
        try {
            validator.validateOwnerJoiningLeague("ownerInLeague", "test");
            Assert.fail();
        } catch (AlreadyInLeague ignored) { }
    }

}