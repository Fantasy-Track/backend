package usecase;

import domain.entity.Team;
import domain.exception.ApplicationException;
import domain.repository.PickOrderRepository;
import domain.repository.TeamRepository;
import domain.repository.mock.MockPickOrderRepository;
import mock.MockDistributedLock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class SetupRandomPickOrderTest {

    @org.testng.annotations.Test
    public void testSetup() throws ApplicationException {
        PickOrderRepository pickOrderRepository = new MockPickOrderRepository();
        TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
        when(teamRepository.getTeamsInLeague("test")).then(invocation -> List.of(
                Team.builder().id("T1").build(),
                Team.builder().id("T2").build(),
                Team.builder().id("T3").build()));

        SetupRandomPickOrder setupPickOrder = new SetupRandomPickOrder(pickOrderRepository, teamRepository, new MockDistributedLock(), "test");

        assertNull(pickOrderRepository.getPickOrder());

        setupPickOrder.setup();

        List<String> pickOrder = pickOrderRepository.getPickOrder();
        assertEquals(pickOrder.size(), 3);
        assertTrue(pickOrder.contains("T1"));
        assertTrue(pickOrder.contains("T2"));
        assertTrue(pickOrder.contains("T3"));
    }
}