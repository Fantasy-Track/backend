package domain.entity;

public class DraftPosition {

    public final int round, pick;

    public DraftPosition(int round, int pick) {
        this.round = round;
        this.pick = pick;
    }

    public DraftPosition getNextPosition(int roundLength) {
        int round = this.round;
        int pick = this.pick + 1;
        if (pick > roundLength) {
            round++;
            pick = 1;
        }
        return new DraftPosition(round, pick);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DraftPosition)) return false;
        return ((DraftPosition) obj).pick == pick && ((DraftPosition) obj).round == round;
    }
}
