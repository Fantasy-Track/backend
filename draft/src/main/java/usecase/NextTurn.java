package usecase;

import com.google.inject.Inject;
import domain.entity.Turn;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextTurn {

    private final Logger logger = LoggerFactory.getLogger(NextTurn.class);

    private final TurnRepository turnRepository;
    private final TurnRotation turnRotation;
    private final SettingsRepository settingsRepository;

    @Inject
    public NextTurn(TurnRepository turnRepository, TurnRotation turnRotation, SettingsRepository settingsRepository) {
        this.turnRepository = turnRepository;
        this.turnRotation = turnRotation;
        this.settingsRepository = settingsRepository;
    }

    public void next() {
        Turn currentTurn = turnRepository.getCurrentTurn();
        Turn nextTurn = turnRotation.nextInRotation(currentTurn);
        if (settingsRepository.getRounds() < nextTurn.position.round) {
            settingsRepository.setDraftStatus(DraftStatus.FINISHED);
            return;
        }
        logger.info("Changing turn to: " + nextTurn);
        turnRepository.saveCurrentTurn(nextTurn);
    }

}
