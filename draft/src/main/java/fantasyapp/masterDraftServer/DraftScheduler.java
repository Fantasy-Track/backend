package fantasyapp.masterDraftServer;

import domain.entity.ScheduledDraft;
import domain.repository.ScheduledDraftRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import util.DistributedLock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DraftScheduler {

    private Logger logger = LoggerFactory.getLogger(DraftScheduler.class);

    private ScheduledDraftRepository draftRepository;
    private DraftMasterServer draftMaster;
    private DistributedLock lock;

    public DraftScheduler(ScheduledDraftRepository draftRepository, DraftMasterServer draftMaster, DistributedLock lock) {
        this.draftRepository = draftRepository;
        this.draftMaster = draftMaster;
        this.lock = lock;
    }

    public void startScheduling() throws InterruptedException {
        String cronExpression = "0 0/5 * * * ?";
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.schedule(this::startDraftRooms, new CronTrigger(cronExpression));
    }

    @SneakyThrows
    private void startDraftRooms() {
        lock.lockAndRun(DistributedLock.SCHED_DRAFTS, DistributedLock.ALL_LEAGUES, () -> {
            logger.info("Checking for drafts to start");
            List<ScheduledDraft> drafts = draftRepository.getDraftsBefore(Instant.now().plus(6, ChronoUnit.MINUTES));
            for (ScheduledDraft draft : drafts) {
                draftMaster.startDraftServerForLeague(draft.id);
                draftRepository.deleteScheduledDraft(draft.id);
            }
        });
    }

}
