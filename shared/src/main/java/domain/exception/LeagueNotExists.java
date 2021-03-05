package domain.exception;

import io.grpc.Status;

public class LeagueNotExists extends ApplicationException {

    public LeagueNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "League doesn't exist";
    }
}
