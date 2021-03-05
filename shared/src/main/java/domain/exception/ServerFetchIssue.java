package domain.exception;

import io.grpc.Status;

public class ServerFetchIssue extends ApplicationException {

    public ServerFetchIssue() {
        super(Status.RESOURCE_EXHAUSTED);
    }

    @Override
    public String getMessage() {
        return "Server was unable to fetch data. Try again later.";
    }
}
