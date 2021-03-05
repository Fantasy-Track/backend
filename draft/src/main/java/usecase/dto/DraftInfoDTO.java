package usecase.dto;

import java.time.Instant;
import java.util.List;

public class DraftInfoDTO {

    private List<String> pickingOrder;
    private int rounds;
    private Instant startTime;

    public List<String> getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(List<String> pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}
