package domain.exception;

import io.grpc.Status;

public class NameTaken extends ApplicationException {

    public NameTaken() {
        super(Status.ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return "That name is taken";
    }
}
