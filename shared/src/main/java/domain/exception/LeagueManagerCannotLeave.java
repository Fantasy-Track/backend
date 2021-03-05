package domain.exception;

import io.grpc.Status;

public class LeagueManagerCannotLeave extends ApplicationException {

    public LeagueManagerCannotLeave() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "The league manager cannot leave";
    }
}
