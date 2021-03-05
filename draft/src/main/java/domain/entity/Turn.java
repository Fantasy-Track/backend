package domain.entity;

import lombok.Builder;

@Builder
public class Turn {

    public final DraftPosition position;
    public final String teamId;
    private int timeLeftMillis;
    private boolean hasPicked = false;

    public boolean isHasPicked() {
        return hasPicked;
    }

    public void setHasPicked(boolean hasPicked) {
        this.hasPicked = hasPicked;
    }

    public int getTimeLeftMillis() {
        return timeLeftMillis;
    }

    public void reduceTimeLeft(int delta) {
        this.timeLeftMillis -= delta;
    }

    @Override
    public String toString() {
        return String.format("Team ID: %s", teamId);
    }

}
