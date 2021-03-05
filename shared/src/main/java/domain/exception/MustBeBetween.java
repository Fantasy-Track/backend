package domain.exception;

import io.grpc.Status;

public class MustBeBetween extends ApplicationException {

    final int min, max;
    final String units;

    public MustBeBetween(int min, int max, String units) {
        super(Status.OUT_OF_RANGE);
        this.min = min;
        this.max = max;
        this.units = units;
    }

    @Override
    public String getMessage() {
        return String.format("Must be between %d and %d %s", min, max, units);
    }
}
