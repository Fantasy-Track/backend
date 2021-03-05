package domain.exception;

import io.grpc.Status;
import io.grpc.StatusException;

public abstract class ApplicationException extends StatusException {

    public ApplicationException(Status status) {
        super(status);
    }

    @Override
    public abstract String getMessage();

}
