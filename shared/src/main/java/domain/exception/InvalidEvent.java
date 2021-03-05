package domain.exception;

import io.grpc.Status;

public class InvalidEvent extends ApplicationException {

    public InvalidEvent() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "Event invalid";
    }
}
