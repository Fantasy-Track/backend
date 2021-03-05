package domain.exception;

import io.grpc.Status;

public class OwnerAlreadyExists extends ApplicationException {

    public OwnerAlreadyExists() {
        super(Status.ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return "That account is already registered";
    }
}
