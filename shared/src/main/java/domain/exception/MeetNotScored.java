package domain.exception;

import io.grpc.Status;

public class MeetNotScored extends ApplicationException {

    public MeetNotScored() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Meet has not yet been scored";
    }
}
