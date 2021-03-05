package domain.exception;

import io.grpc.Status;

public class AthleteNotInLeague extends ApplicationException {
    public AthleteNotInLeague() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Athlete is not in league";
    }
}
