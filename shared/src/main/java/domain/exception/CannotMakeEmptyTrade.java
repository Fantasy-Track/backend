package domain.exception;

import io.grpc.Status;

public class CannotMakeEmptyTrade extends ApplicationException {

    public CannotMakeEmptyTrade() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Cannot make empty trade";
    }
}
