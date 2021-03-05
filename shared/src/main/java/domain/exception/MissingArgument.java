package domain.exception;

import io.grpc.Status;

public class MissingArgument extends ApplicationException {

    public MissingArgument() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "Error: missing argument";
    }
}
