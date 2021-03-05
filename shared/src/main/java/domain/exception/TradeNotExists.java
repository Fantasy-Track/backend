package domain.exception;

import io.grpc.Status;

public class TradeNotExists extends ApplicationException {

    public TradeNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Trade proposal does not exist. It may have been cancelled.";
    }
}
