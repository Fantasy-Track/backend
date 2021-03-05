package usecase;

import fantasyapp.masterDraftServer.DraftRegistrar;
import com.google.inject.Inject;
import domain.entity.Turn;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftingProgram {

    private final Logger logger = LoggerFactory.getLogger(DraftingProgram.class);

    private TurnRepository turnRepository;
    private final NextTurn nextTurn;
    private final SettingsRepository settingsRepository;
    private TimeoutBehavior timeoutBehavior;
    private DraftContext context;

    @Inject
    public DraftingProgram(TurnRepository turnRepository, NextTurn nextTurn, SettingsRepository settingsRepository, TimeoutBehavior timeoutBehavior, DraftContext context) {
        this.turnRepository = turnRepository;
        this.nextTurn = nextTurn;
        this.settingsRepository = settingsRepository;
        this.timeoutBehavior = timeoutBehavior;
        this.context = context;
    }

    public void loop() {
        LoggerFactory.getLogger(DraftingProgram.class).info("Starting the Draft");

        settingsRepository.setDraftStatus(DraftStatus.RUNNING);
        nextTurn.next();
        runLoop();

        logger.info("Draft Finished! Exiting the loop");
    }

    private void runLoop() {
        long last = System.currentTimeMillis();
        while (settingsRepository.getDraftStatus() == DraftStatus.RUNNING) {
            Turn turn = turnRepository.getCurrentTurn();
            while (!turn.isHasPicked() && turn.getTimeLeftMillis() > 0) {
                synchronized (DraftRegistrar.draftLocks.get(context.leagueId)) {
                    try {
                        DraftRegistrar.draftLocks.get(context.leagueId).wait(turn.getTimeLeftMillis());
                    } catch (InterruptedException e) { }
                }
                long currTime = System.currentTimeMillis();
                int delta = (int) (currTime - last);
                turn.reduceTimeLeft(delta);
                last = currTime;
            }

            timeoutBehavior.handleTimeout(turn);
            nextTurn.next();
        };
    }

}
