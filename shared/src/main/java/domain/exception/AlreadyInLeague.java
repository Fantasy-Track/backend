package domain.exception;

import io.grpc.Status;

public class AlreadyInLeague extends ApplicationException {

    public AlreadyInLeague() {
        super(Status.ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return "You are already in this league";
    }
}
