package domain.exception;

import io.grpc.Status;

public class AccountNotFound extends ApplicationException {

    public AccountNotFound() {
        super(Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Account not found. Please sign up.";
    }
}
