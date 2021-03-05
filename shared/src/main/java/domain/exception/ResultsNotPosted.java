package domain.exception;

import io.grpc.Status;

public class ResultsNotPosted extends ApplicationException {

    public ResultsNotPosted() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Results are not yet posted";
    }
}
