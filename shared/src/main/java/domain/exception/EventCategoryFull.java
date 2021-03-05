package domain.exception;

import io.grpc.Status;

public class EventCategoryFull extends ApplicationException {

    public EventCategoryFull() {
        super(Status.FAILED_PRECONDITION);
    }

    @Override
    public String getMessage() {
        return "Event category is full";
    }
}
