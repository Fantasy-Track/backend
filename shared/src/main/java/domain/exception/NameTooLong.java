package domain.exception;

import io.grpc.Status;

public class NameTooLong extends ApplicationException {

    public NameTooLong() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "That name is too long";
    }
}
