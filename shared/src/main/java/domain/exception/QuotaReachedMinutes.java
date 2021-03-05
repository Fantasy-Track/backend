package domain.exception;

import io.grpc.Status;

public class QuotaReachedMinutes extends ApplicationException {

    private int mins;

    public QuotaReachedMinutes(int mins) {
        super(Status.RESOURCE_EXHAUSTED);
        this.mins = mins;
    }

    @Override
    public String getMessage() {
        return "You may only do that every " + mins + " minutes.";
    }
}
