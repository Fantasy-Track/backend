package domain.exception;

import io.grpc.Status;

public class MeetBeforeDraft extends ApplicationException {

    public MeetBeforeDraft() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Cannot enable meets before your draft time.";
    }
}
