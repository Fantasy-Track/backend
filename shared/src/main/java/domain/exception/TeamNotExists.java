package domain.exception;

import io.grpc.Status;

public class TeamNotExists extends ApplicationException {

    public TeamNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Team doesn't exist";
    }
}
