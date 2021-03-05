package usecase.notification;

import com.google.firebase.messaging.*;
import com.google.inject.Inject;
import domain.entity.Meet;
import domain.repository.NameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationHandler {

    private Logger logger = LoggerFactory.getLogger(NotificationHandler.class);

    private final FirebaseMessaging messaging;
    private NameRepository nameRepository;

    @Inject
    public NotificationHandler(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
        messaging = FirebaseMessaging.getInstance();
    }

    public void sendDraftingSoon(String leagueId) {
        try {
            messaging.send(Message.builder()
                    .setTopic(leagueId)
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("drafting_soon_notification").build())
                    .putData("android_channel_id", "draft")
                    .setNotification(new Notification(
                            nameRepository.getLeagueName(leagueId),
                            "Your league is drafting soon!"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending draft notification", e);
        }
    }

    public void sendTradeRejected(String proposingTeamId, String acceptingTeamId) {
        try {
            messaging.send(Message.builder()
                    .setTopic(proposingTeamId)
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("trade_rejected_notification").build())
                    .putData("android_channel_id", "trade")
                    .setNotification(new Notification(
                            "Trade Rejected",
                            nameRepository.getTeamName(acceptingTeamId) + " has rejected your trade"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending trade reject notification", e);
        }
    }

    public void sendTradeAccepted(String proposingTeamId, String acceptingTeamId) {
        try {
            messaging.send(Message.builder()
                    .setTopic(proposingTeamId)
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("trade_accepted_notification").build())
                    .putData("android_channel_id", "trade")
                    .setNotification(new Notification(
                            "Trade Accepted",
                            nameRepository.getTeamName(acceptingTeamId) + " has accepted your trade! It will process soon"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending trade accept notification", e);
        }
    }

    public void sendTradeProposed(String proposingTeamId, String acceptingTeamId) {
        try {
            messaging.send(Message.builder()
                    .setTopic(acceptingTeamId)
                    .putData("android_channel_id", "trade")
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("trade_proposed_notification").build())
                    .setNotification(new Notification(
                            nameRepository.getTeamName(proposingTeamId) + " proposed a trade",
                            "Tap to review"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending trade propose notification", e);
        }
    }

    public void sendTradeProcessed(String teamId) {
        try {
            messaging.send(Message.builder()
                    .setTopic(teamId)
                    .putData("android_channel_id", "trade")
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("trade_processed_notification").build())
                    .setNotification(new Notification(
                            "Trade Processed",
                            "The athlete(s) have been added to your bench. Don't forget to update your roster!"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending trade accept notification", e);
        }
    }

    public void sendMeetProcessed(Meet meet) {
        try {
            messaging.send(Message.builder()
                    .setTopic(meet.leagueId)
                    .putData("android_channel_id", "meet")
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("meet_processed_notification").build())
                    .setNotification(new Notification(
                            "Meet Results Posted",
                            "Meet scores have been computed and are ready to view!"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending meet processed notification", e);
        }
    }

    public void sendUpcomingMeet(Meet meet) {
        try {
            messaging.send(Message.builder()
                    .setTopic(meet.leagueId)
                    .putData("android_channel_id", "meet")
                    .setFcmOptions(FcmOptions.builder().setAnalyticsLabel("upcoming_meet_notification").build())
                    .setNotification(new Notification(
                            meet.name,
                            "Make sure to finalize your roster for the meet tomorrow!"
                    ))
                    .build());
        } catch (Exception e) {
            logger.error("Error sending upcoming meet notification", e);
        }
    }
}
