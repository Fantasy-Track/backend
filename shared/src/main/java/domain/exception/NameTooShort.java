package domain.exception;

import io.grpc.Status;

public class NameTooShort extends ApplicationException {

    public NameTooShort() {
        super(Status.INVALID_ARGUMENT);
    }


    @Override
    public String getMessage() {
        return "Name is too short";
    }
}
