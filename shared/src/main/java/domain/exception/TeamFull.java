package domain.exception;

import io.grpc.Status;

public class TeamFull extends ApplicationException{

    public TeamFull() {
        super(Status.RESOURCE_EXHAUSTED);
    }

    @Override
    public String getMessage() {
        return "Team is full. Please drop athletes";
    }
}
