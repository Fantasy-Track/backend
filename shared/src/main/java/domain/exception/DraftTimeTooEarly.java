package domain.exception;

import io.grpc.Status;

public class DraftTimeTooEarly extends ApplicationException{

    public DraftTimeTooEarly() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "You must schedule the draft for a later time";
    }
}
