package domain.exception;

import io.grpc.Status;

public class NotCurrentlyPicking extends ApplicationException {

    public NotCurrentlyPicking() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "This team is not currently picking";
    }
}
