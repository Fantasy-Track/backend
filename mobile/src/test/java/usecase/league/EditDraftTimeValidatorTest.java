package usecase.league;

import domain.entity.League;
import domain.entity.ScheduledDraft;
import domain.exception.AlreadyDrafted;
import domain.exception.ApplicationException;
import domain.exception.CannotDoActionNow;
import domain.repository.LeagueRepository;
import domain.repository.ScheduledDraftRepository;
import mock.LeagueBank;
import mock.MockDistributedLock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditDraftTimeValidatorTest {

    private EditDraftTimeValidator validator;
    private LeagueRepository leagueRepository;
    private ScheduledDraftRepository draftRepository;

    @BeforeMethod
    public void setUp() {
        leagueRepository = mock(LeagueRepository.class);
        draftRepository = mock(ScheduledDraftRepository.class);
        validator = new EditDraftTimeValidator(leagueRepository, draftRepository, new MockDistributedLock());
    }

    @Test
    public void testLeagueNotPreDraft() throws ApplicationException {
        when(leagueRepository.getLeagueById(LeagueBank.postDraftLeague.id)).then(invocation -> LeagueBank.postDraftLeague);
        try {
            validator.validateCanEditDraftTime(LeagueBank.postDraftLeague.id);
            Assert.fail();
        } catch (CannotDoActionNow ignored) { }
    }

    @Test
    public void testNoDraftScheduled() throws ApplicationException {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(draftRepository.getDraftForLeague(league.id)).then(invocation -> null);
        try {
            validator.validateCanEditDraftTime(league.id);
            Assert.fail();
        } catch (AlreadyDrafted ignored) { }
    }

    @Test
    public void testEditOk() throws ApplicationException {
        League league = LeagueBank.predraftLeague;
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(draftRepository.getDraftForLeague(league.id)).then(invocation -> ScheduledDraft.builder()
                .id(league.id)
                .time(Instant.now()).build());

        validator.validateCanEditDraftTime(league.id);
    }

}