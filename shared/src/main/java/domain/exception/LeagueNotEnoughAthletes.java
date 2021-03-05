package domain.exception;

import io.grpc.Status;

public class LeagueNotEnoughAthletes extends ApplicationException {

    public LeagueNotEnoughAthletes() {
        super(Status.OUT_OF_RANGE);
    }

    @Override
    public String getMessage() {
        return "League doesn't have enough athletes for that many teams.";
    }
}
