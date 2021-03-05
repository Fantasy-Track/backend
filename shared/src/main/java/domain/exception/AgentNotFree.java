package domain.exception;

import io.grpc.Status;

public class AgentNotFree extends ApplicationException {

    public AgentNotFree() {
        super(Status.INVALID_ARGUMENT);
    }

    @Override
    public String getMessage() {
        return "Athlete is not a free agent!";
    }

}
