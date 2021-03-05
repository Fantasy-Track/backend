package usecase;

import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import domain.repository.mock.MockSettingsRepository;
import domain.repository.mock.TurnBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class NextTurnTest {

    private SettingsRepository settingsRepository;
    private NextTurn nextTurn;
    private TurnRotation turnRotation;
    private TurnRepository turnRepository;

    @BeforeMethod
    public void setUp() {
        settingsRepository = new MockSettingsRepository(1);
        turnRepository = mock(TurnRepository.class);
        turnRotation = mock(TurnRotation.class);
        nextTurn = new NextTurn(turnRepository, turnRotation, settingsRepository);
    }

    @Test
    public void testNext() {
        when(turnRepository.getCurrentTurn()).then(invocation -> TurnBank.TURN1);
        when(turnRotation.nextInRotation(TurnBank.TURN1)).then(invocation -> TurnBank.TURN2);

        settingsRepository.setDraftStatus(DraftStatus.RUNNING);

        nextTurn.next();

        verify(turnRepository).saveCurrentTurn(TurnBank.TURN2);
        assertEquals(settingsRepository.getDraftStatus(), DraftStatus.RUNNING);
    }

    @Test
    public void testNextWhenDraftFinished() {
        when(turnRepository.getCurrentTurn()).then(invocation -> TurnBank.TURN2);
        when(turnRotation.nextInRotation(TurnBank.TURN2)).then(invocation -> TurnBank.TURN3);

        settingsRepository.setDraftStatus(DraftStatus.RUNNING);

        nextTurn.next();

        verify(turnRepository, times(0)).saveCurrentTurn(any());
        assertEquals(settingsRepository.getDraftStatus(), DraftStatus.FINISHED);
    }

}