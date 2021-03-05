package usecase;

import domain.repository.TurnRepository;
import domain.repository.mock.TurnBank;
import org.testng.annotations.Test;
import usecase.mock.MockTimeoutBehavior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class TurnUpdaterTest {

    @Test
    public void testTimeoutIfShould() {
        MockTimeoutBehavior timeoutBehavior = new MockTimeoutBehavior();
        TurnRepository turnRepository = mock(TurnRepository.class);
        when(turnRepository.getCurrentTurn()).then(invocation -> TurnBank.TURN1);

        TurnUpdater turnUpdater = new TurnUpdater(timeoutBehavior, turnRepository);

        turnUpdater.updateTurn(1500);

        assertEquals(timeoutBehavior.calledWithTurn, TurnBank.TURN1);
        assertTrue(timeoutBehavior.called);
    }

    @Test
    public void testDoesNotTimeout() {
        MockTimeoutBehavior timeoutBehavior = new MockTimeoutBehavior();
        TurnRepository turnRepository = mock(TurnRepository.class);
        when(turnRepository.getCurrentTurn()).then(invocation -> TurnBank.TURN1);

        TurnUpdater turnUpdater = new TurnUpdater(timeoutBehavior, turnRepository);

        turnUpdater.updateTurn(0);

        assertFalse(timeoutBehavior.called);
        assertNull(timeoutBehavior.calledWithTurn);
    }

}