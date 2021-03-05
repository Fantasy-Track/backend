package usecase;

import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.AlreadyDrafted;
import domain.exception.ApplicationException;
import domain.repository.LeagueRepository;
import mock.LeagueBank;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DraftSetupTeardownTest {

    @Test
    public void testSetupTeardown() throws ApplicationException {
        League league = LeagueBank.predraftLeague;

        SetupRandomPickOrder pickOrder = Mockito.mock(SetupRandomPickOrder.class);
        LeagueRepository leagueRepository = Mockito.mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        DraftSetupTeardown draftSetupTeardown = new DraftSetupTeardown(pickOrder, leagueRepository, league.id);

        draftSetupTeardown.setup();

        verify(pickOrder).setup();
        verify(leagueRepository).setStatus(league.id, LeagueStatus.DRAFTING);

        draftSetupTeardown.teardown();

        verify(leagueRepository).setStatus(league.id, LeagueStatus.POST_DRAFT);
    }

    @Test
    public void testAlreadyDrafted() throws ApplicationException {
        League league = LeagueBank.postDraftLeague;

        SetupRandomPickOrder pickOrder = Mockito.mock(SetupRandomPickOrder.class);
        LeagueRepository leagueRepository = Mockito.mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);

        DraftSetupTeardown draftSetupTeardown = new DraftSetupTeardown(pickOrder, leagueRepository, league.id);

        try {
            draftSetupTeardown.setup();
            Assert.fail();
        } catch (AlreadyDrafted ignored) {}
    }

}