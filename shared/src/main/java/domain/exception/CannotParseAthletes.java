package domain.exception;

import io.grpc.Status;

public class CannotParseAthletes extends ApplicationException {

    public CannotParseAthletes() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "Error parsing athletes";
    }
}
