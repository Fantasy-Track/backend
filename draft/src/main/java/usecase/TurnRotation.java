package usecase;

import domain.entity.Turn;

public interface TurnRotation {

    Turn nextInRotation(Turn currentTurn);

}
