package util;

import com.google.api.client.util.DateTime;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class TimeUtil {

    public static boolean isAfterPST(Instant time, Instant pstDate) {
        ZonedDateTime pstTime = time.atZone(ZoneId.of("America/Los_Angeles"));
        ZonedDateTime zonedPSTDate = pstDate.atZone(ZoneOffset.UTC).withZoneSameLocal(ZoneId.of("America/Los_Angeles"));
        return pstTime.isAfter(zonedPSTDate);
    }

}
