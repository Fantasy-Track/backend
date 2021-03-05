package usecase.mock;

import domain.entity.Turn;
import usecase.TurnRotation;

// Mock class obviously only works for a single use
public class MockTurnRotation implements TurnRotation {

    private Turn nextTurn;

    public MockTurnRotation(Turn nextTurn) {
        this.nextTurn = nextTurn;
    }


    @Override
    public Turn nextInRotation(Turn currentTurn) {
        return nextTurn;
    }

}
