package domain.exception;

import io.grpc.Status;

public class SchoolNotEnoughAthletes extends ApplicationException {

    public SchoolNotEnoughAthletes() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "School does not have enough athletes to create a league.";
    }
}
