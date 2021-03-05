package usecase;

import com.google.inject.Inject;
import domain.entity.Turn;
import domain.repository.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurnUpdater {

    private final Logger logger = LoggerFactory.getLogger(TurnUpdater.class);

    private final TimeoutBehavior timeoutBehavior;
    private final TurnRepository turnRepository;

    @Inject
    public TurnUpdater(TimeoutBehavior timeoutBehavior, TurnRepository turnRepository) {
        this.timeoutBehavior = timeoutBehavior;
        this.turnRepository = turnRepository;
    }

    public void updateTurn(int delta) {
        Turn turn = turnRepository.getCurrentTurn();
        turn.reduceTimeLeft(delta);
        if (turn.getTimeLeftMillis() > 0) return;

        logger.info("Handling timeout for turn: " + turn);
        timeoutBehavior.handleTimeout(turn);
    }


}
