package domain.exception;

import io.grpc.Status;

public class CannotParse extends ApplicationException {

    private final String item;

    public CannotParse(String item) {
        super(Status.INVALID_ARGUMENT);
        this.item = item;
    }

    @Override
    public String getMessage() {
        return "Cannot parse " + item;
    }
}
