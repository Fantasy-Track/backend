package fantasyapp.grpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class GrpcError {

    public static StatusRuntimeException makeError(Throwable throwable) {
        return Status.fromThrowable(throwable).withDescription(throwable.getMessage()).asRuntimeException();
    }

    public static StatusRuntimeException makeError(Throwable throwable, int code) {
        return Status.fromCodeValue(code).withDescription(throwable.getMessage()).asRuntimeException();
    }

}
