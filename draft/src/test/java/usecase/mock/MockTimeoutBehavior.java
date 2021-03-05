package usecase.mock;

import com.mongodb.lang.NonNull;
import domain.entity.Turn;
import usecase.TimeoutBehavior;

public class MockTimeoutBehavior implements TimeoutBehavior {

    public Turn calledWithTurn = null;
    public boolean called = false;

    @Override
    public void handleTimeout(@NonNull Turn turn) {
        called = true;
        calledWithTurn = turn;
    }
}
