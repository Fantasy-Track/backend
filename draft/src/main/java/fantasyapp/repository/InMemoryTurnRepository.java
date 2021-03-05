package fantasyapp.repository;

import fantasyapp.repository.listeners.TurnChangeListener;
import domain.entity.Turn;
import domain.repository.TurnRepository;

import javax.inject.Inject;

public class InMemoryTurnRepository implements TurnRepository {

    private Turn currentTurn;
    private TurnChangeListener turnChangeListener;

    @Inject
    public InMemoryTurnRepository(TurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    @Override
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public void saveCurrentTurn(Turn turn) {
        currentTurn = turn;
        if (turnChangeListener != null) {
            turnChangeListener.turnChanged(turn);
        }
    }

}
