package domain.exception;

import io.grpc.Status;

public class MeetNotExists extends ApplicationException {

    public MeetNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Meet doesn't exist.";
    }
}
