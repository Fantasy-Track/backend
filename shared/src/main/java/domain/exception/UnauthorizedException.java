package domain.exception;

import io.grpc.Status;

public class UnauthorizedException extends ApplicationException {

    public UnauthorizedException() {
        super(Status.PERMISSION_DENIED);
    }

    @Override
    public String getMessage() {
        return "You are not authorized to do that.";
    }
}
