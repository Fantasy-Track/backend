package domain.exception;

import io.grpc.Status;

public class CannotDoActionNow extends ApplicationException {
    public CannotDoActionNow() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "You cannot do that action now";
    }
}
