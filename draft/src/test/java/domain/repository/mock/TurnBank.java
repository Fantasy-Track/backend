package domain.repository.mock;

import domain.entity.DraftPosition;
import domain.entity.Turn;

public class TurnBank {

    public static final Turn TURN1 = Turn.builder().teamId("T1").timeLeftMillis(1000).position(new DraftPosition(1,1)).build();
    public static final Turn TURN2 = Turn.builder().teamId("T2").timeLeftMillis(1000).position(new DraftPosition(1,2)).build();
    public static final Turn TURN3 = Turn.builder().teamId("T2").timeLeftMillis(1000).position(new DraftPosition(2,1)).build();

}
