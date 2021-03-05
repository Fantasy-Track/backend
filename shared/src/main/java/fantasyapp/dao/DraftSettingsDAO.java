package fantasyapp.dao;

import java.time.Instant;

public class DraftSettingsDAO {

    private Instant startTime;
    private int rounds;
    private int turnDurationMillis;

    public int getTurnDurationMillis() {
        return turnDurationMillis;
    }

    public void setTurnDurationMillis(int turnDurationMillis) {
        this.turnDurationMillis = turnDurationMillis;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
