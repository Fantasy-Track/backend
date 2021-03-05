package domain.exception;

import io.grpc.Status;

public class TeamNotInLeague extends ApplicationException {

    public TeamNotInLeague() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Team doesn't exist in league";
    }
}
