package domain.exception;

import io.grpc.Status;

public class TradeNotAccepted extends ApplicationException {

    public TradeNotAccepted() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Trade was not yet accepted";
    }
}
