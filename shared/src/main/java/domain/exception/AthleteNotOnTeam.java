package domain.exception;

import io.grpc.Status;

public class AthleteNotOnTeam extends ApplicationException {

    public AthleteNotOnTeam() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Athlete is not on team";
    }
}
