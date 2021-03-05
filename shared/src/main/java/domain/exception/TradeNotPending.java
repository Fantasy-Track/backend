package domain.exception;

import io.grpc.Status;

public class TradeNotPending extends ApplicationException {

    public TradeNotPending() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Trade has already been accepted, rejected, or cancelled";
    }
}
