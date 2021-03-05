package domain.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DraftPositionTest {

    @Test
    public void testGetNextPositionAtEndOfRound() {
        DraftPosition firstPosition = new DraftPosition(1, 3);
        DraftPosition secondPosition = new DraftPosition(2, 1);

        DraftPosition nextPosition = firstPosition.getNextPosition(3);
        assertEquals(nextPosition, secondPosition);
    }

    @Test
    public void testGetNextPositionAtBottomOfRound() {
        DraftPosition firstPosition = new DraftPosition(1, 3);
        DraftPosition secondPosition = new DraftPosition(1, 4);

        DraftPosition nextPosition = firstPosition.getNextPosition(4);
        assertEquals(nextPosition, secondPosition);
    }
}