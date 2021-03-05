package domain.exception;

import io.grpc.Status;

public class CannotTradeWithSelf extends ApplicationException {

    public CannotTradeWithSelf() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "You cannot trade with yourself";
    }
}
