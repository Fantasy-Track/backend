package usecase.meetLocking;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

public class UpcomingMeetNotifier {

    private Logger logger = LoggerFactory.getLogger(UpcomingMeetNotifier.class);
    private MeetRepository meetRepository;

    @Inject
    public UpcomingMeetNotifier(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    public void sendNotificationForUpcomingMeets() {
        logger.info("Sending upcoming meet notifications");
        ZonedDateTime pstTime = Instant.now().atZone(ZoneId.of("America/Los_Angeles"));

        Instant tomorrow = pstTime.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC).plus(1, ChronoUnit.DAYS);
        Instant dayAfterTomorrow = tomorrow.plus(1, ChronoUnit.DAYS);

        List<Meet> meets = meetRepository.getEnabledUnlockedMeetsBetween(tomorrow, dayAfterTomorrow);
        for (Meet meet : meets) {
            logger.info("Sending notification for meet: " + meet.toString());
            NotificationFactory.handler.sendUpcomingMeet(meet);
        }
    }

}
