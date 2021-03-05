package usecase;

import com.google.inject.Inject;
import domain.entity.DraftPosition;
import domain.entity.Turn;
import domain.repository.PickOrderRepository;
import domain.repository.SettingsRepository;

import java.util.List;

public class SnakeTurnRotation implements TurnRotation {

    private final PickOrderRepository pickOrderRepository;
    private final SettingsRepository repository;

    @Inject
    public SnakeTurnRotation(SettingsRepository settingsRepository, PickOrderRepository pickOrderRepository) {
        this.repository = settingsRepository;
        this.pickOrderRepository = pickOrderRepository;
    }

    public Turn nextInRotation(Turn currentTurn) {
        DraftPosition currentPosition = currentTurn == null ? new DraftPosition(1, 0) : currentTurn.position;

        List<String> pickingOrder = pickOrderRepository.getPickOrder();
        assert pickingOrder != null;

        DraftPosition nextPosition = currentPosition.getNextPosition(pickingOrder.size());
        String teamId = pickingOrder.get(convertPositionToIndex(nextPosition, pickingOrder.size()));

        return Turn.builder()
                .teamId(teamId)
                .position(nextPosition)
                .timeLeftMillis(repository.getTurnMillis())
                .build();
    }

    // specific for the snake draft
    private int convertPositionToIndex(DraftPosition position, int roundLength) {
        if (position.round % 2 == 0)
            return roundLength - position.pick;
        return position.pick - 1;
    }


}
