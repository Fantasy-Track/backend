package domain.exception;

import io.grpc.Status;

public class AlreadyDrafted extends ApplicationException {

    public AlreadyDrafted() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "League has already started drafting";
    }
}
