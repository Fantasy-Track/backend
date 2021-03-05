package usecase;

import domain.entity.DraftPosition;
import domain.entity.Turn;
import domain.repository.PickOrderRepository;
import domain.repository.SettingsRepository;
import domain.repository.mock.MockSettingsRepository;
import domain.repository.mock.MockStaticPickOrderRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SnakeTurnRotationTest {

    @Test
    public void testNextInRotation() {
        PickOrderRepository pickOrderRepository = new MockStaticPickOrderRepository();

        SettingsRepository settingsRepository = new MockSettingsRepository(5);

        assertEquals(pickOrderRepository.getPickOrder(), new ArrayList<>(List.of("1", "2")));

        SnakeTurnRotation turnRotation = new SnakeTurnRotation(settingsRepository, pickOrderRepository);
        Turn nextTurn = turnRotation.nextInRotation(null);

        assertEquals(nextTurn.position, new DraftPosition(1, 1));
        assertEquals(nextTurn.teamId, "1");
        assertEquals(nextTurn.getTimeLeftMillis(), 3000);

        nextTurn = turnRotation.nextInRotation(nextTurn);

        assertEquals(nextTurn.position, new DraftPosition(1, 2));
        assertEquals(nextTurn.getTimeLeftMillis(), 3000);
        assertEquals(nextTurn.teamId, "2");

        nextTurn = turnRotation.nextInRotation(nextTurn);

        assertEquals(nextTurn.position, new DraftPosition(2, 1));
        assertEquals(nextTurn.teamId, "2");
        assertEquals(nextTurn.getTimeLeftMillis(), 3000);

        nextTurn = turnRotation.nextInRotation(nextTurn);

        assertEquals(nextTurn.position, new DraftPosition(2, 2));
        assertEquals(nextTurn.teamId, "1");
        assertEquals(nextTurn.getTimeLeftMillis(), 3000);
    }
}