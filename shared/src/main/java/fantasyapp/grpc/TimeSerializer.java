package fantasyapp.grpc;

import com.google.protobuf.Timestamp;

import java.time.Instant;

public class TimeSerializer {

    public static Timestamp serializeTime(Instant time) {
        return Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
    }

    public static Instant deserializeTime(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

}
