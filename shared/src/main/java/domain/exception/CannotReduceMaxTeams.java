package domain.exception;

import io.grpc.Status;

public class CannotReduceMaxTeams extends ApplicationException {

    public CannotReduceMaxTeams() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "You must kick teams to reduce the max teams.";
    }
}
