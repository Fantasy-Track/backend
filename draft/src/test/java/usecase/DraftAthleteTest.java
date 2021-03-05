package usecase;

import fantasyapp.masterDraftServer.DraftRegistrar;
import domain.entity.DraftPosition;
import domain.entity.Turn;
import domain.exception.ApplicationException;
import domain.exception.NotCurrentlyPicking;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class DraftAthleteTest {

    @Mock
    private ContractEditor contractEditor;
    @Mock
    private TurnRepository turnRepository;
    @Mock
    private SettingsRepository settingsRepository;

    private Turn turn;

    @BeforeMethod
    public void setup() {
        DraftRegistrar.draftLocks.put("test", new Object());
        turn = Turn.builder().teamId("T1").timeLeftMillis(1000).position(new DraftPosition(1,1)).build();
        MockitoAnnotations.initMocks(this);
        when(settingsRepository.getDraftStatus()).then(invocation -> DraftStatus.RUNNING);
        when(turnRepository.getCurrentTurn()).then(invocation -> turn);
    }

    @Test
    public void testDraftFinished() throws ApplicationException {
        when(settingsRepository.getDraftStatus()).then(invocation -> DraftStatus.FINISHED);
        DraftAthlete draftAthlete = new DraftAthlete(turnRepository, contractEditor, new DraftContext("test"), settingsRepository);
        try {
            draftAthlete.draft(ContractRequest.builder()
                    .teamId("T1")
                    .athleteId("A1")
                    .leagueId("test").build());
            Assert.fail();
        } catch (NotCurrentlyPicking ignored) {
            // Expected Behavior
        }

        Mockito.verify(contractEditor, Mockito.times(0)).signContract(Mockito.any());
        assertFalse(turn.isHasPicked());
    }

    @Test
    public void testDraftWhenNotTurn() throws ApplicationException {
        DraftAthlete draftAthlete = new DraftAthlete(turnRepository, contractEditor, new DraftContext("test"), settingsRepository);
        try {
            draftAthlete.draft(ContractRequest.builder()
                    .teamId("T2")
                    .athleteId("A1")
                    .leagueId("test").build());
            Assert.fail();
        } catch (NotCurrentlyPicking ignored) {
            // Expected Behavior
        }

        Mockito.verify(contractEditor, Mockito.times(0)).signContract(Mockito.any());
        assertFalse(turn.isHasPicked());
    }


    @Test
    public void testDraftOnTurn() throws ApplicationException {
        DraftAthlete draftAthlete = new DraftAthlete(turnRepository, contractEditor, new DraftContext("test"), settingsRepository);

        draftAthlete.draft(ContractRequest.builder()
                .teamId("T1")
                .athleteId("A1")
                .leagueId("test").build());

        ArgumentCaptor<ContractRequest> argument = ArgumentCaptor.forClass(ContractRequest.class);
        Mockito.verify(contractEditor, Mockito.times(1)).signContract(argument.capture());
        assertEquals(argument.getValue().teamId, "T1");
        assertEquals(argument.getValue().athleteId, "A1");

        assertTrue(turn.isHasPicked());
    }
}