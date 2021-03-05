package domain.exception;

import io.grpc.Status;

public class UnequalTrade extends ApplicationException {
    public UnequalTrade() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "You must offer and request the same number of athletes.";
    }
}
