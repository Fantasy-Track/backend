package domain.exception;

import io.grpc.Status;

public class AthleteNotExists extends ApplicationException {

    public AthleteNotExists() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Athlete doesn't exist.";
    }
}
