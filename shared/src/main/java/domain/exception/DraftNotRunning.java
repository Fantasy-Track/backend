package domain.exception;

import io.grpc.Status;

public class DraftNotRunning extends ApplicationException {

    public DraftNotRunning() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "The draft is not currently running";
    }
}
