package domain.exception;

import io.grpc.Status;

public class LeagueFull extends ApplicationException {

    public LeagueFull() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "League is full";
    }
}
