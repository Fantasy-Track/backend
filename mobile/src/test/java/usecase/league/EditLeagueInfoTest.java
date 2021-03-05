package usecase.league;

import domain.entity.League;
import domain.exception.*;
import domain.repository.EditLeagueRepository;
import domain.repository.NameRepository;
import domain.repository.TeamRepository;
import mock.LeagueBank;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EditLeagueInfoTest {

    private League league = LeagueBank.postDraftLeague;
    private EditLeagueRepository leagueRepository;
    private LeagueNameValidator nameValidator;
    private EditLeagueInfo editInfo;
    private NameRepository nameRepository;

    @BeforeMethod
    public void setUp() {
        leagueRepository = mock(EditLeagueRepository.class);
        nameValidator = mock(LeagueNameValidator.class);
        nameRepository = mock(NameRepository.class);
        TeamRepository teamRepository = mock(TeamRepository.class);
        editInfo = new EditLeagueInfo(leagueRepository, nameValidator, teamRepository, nameRepository);

        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(teamRepository.countTeamsInLeague(league.id)).then(invocation -> 1);
    }

    @Test
    public void testVerifyCanEdit() throws LeagueNotExists, UnauthorizedException {
        editInfo.verifyCanEdit(league.id, league.owningTeam);
    }


    @Test
    public void testVerifyCannotEdit() throws LeagueNotExists, UnauthorizedException {
        try {
            editInfo.verifyCanEdit(league.id, "notTheLeagueOwner");
            Assert.fail();
        } catch (UnauthorizedException ignored) {
            //ignore
        }
    }

    @Test
    public void editMaxTeamsTooHigh() throws CannotReduceMaxTeams {
        try {
            editInfo.editMaxTeams(league.id, 3);
            Assert.fail();
        } catch (LeagueNotEnoughAthletes ignored) { }
        verify(leagueRepository).updateMaxTeams(league.id, 2);
    }

    @Test
    public void editMaxTeamsTooLow() throws Exception {
        try {
            editInfo.editMaxTeams(league.id, 0);
            Assert.fail();
        } catch (CannotReduceMaxTeams ignored) { }
        verify(leagueRepository).updateMaxTeams(league.id, 1);
    }

    @Test
    public void editMaxTeamsOk() throws Exception {
        editInfo.editMaxTeams(league.id, 2);
        verify(leagueRepository).updateMaxTeams(league.id, 2);
    }

    @Test
    public void testEditName() throws NameTooShort, NameTaken, NameTooLong {
        String name = "New Name";
        editInfo.editName(league.id, name);
        verify(nameValidator).validateName(name);
        verify(nameRepository).updateLeagueName(league.id, name);
    }

    @Test
    public void testTurnDurationLeagueStateNotPreDraft() throws CannotDoActionNow, MustBeBetween {
        try {
            editInfo.editDraftTurnDuration(league.id, 5);
            Assert.fail();
        } catch (CannotDoActionNow ignored) { }
    }

    @Test
    public void testTurnDurationTooHigh() throws ApplicationException {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        try {
            editInfo.editDraftTurnDuration(league.id, 500);
            Assert.fail();
        } catch (MustBeBetween ignored) { }
    }

    @Test
    public void testTurnDurationTooLow() throws ApplicationException {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        try {
            editInfo.editDraftTurnDuration(league.id, 5);
            Assert.fail();
        } catch (MustBeBetween ignored) { }
    }

    @Test
    public void testSetTurnDurationSuccess() throws ApplicationException {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        editInfo.editDraftTurnDuration(league.id, 90);
        verify(leagueRepository).setTurnDurationMillis(league.id, 90000);
    }


}