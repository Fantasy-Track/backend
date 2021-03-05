package domain.exception;

import io.grpc.Status;

public class OwnerNotExists extends ApplicationException {

    public OwnerNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Owner doesn't exist";
    }
}
