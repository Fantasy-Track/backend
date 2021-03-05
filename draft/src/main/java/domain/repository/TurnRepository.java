package domain.repository;

import domain.entity.Turn;

public interface TurnRepository {

    Turn getCurrentTurn();

    void saveCurrentTurn(Turn turn);

}
