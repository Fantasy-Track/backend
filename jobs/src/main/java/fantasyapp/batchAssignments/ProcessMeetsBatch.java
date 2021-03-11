package fantasyapp.batchAssignments;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.repository.MeetRepository;
import fantasyapp.ExecuteJobs;
import fantasyapp.job.MeetProcessingThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.meetProcessing.GenerateMeetReport;
import usecase.postProcess.ReportPostProcessor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

// process all meets that have already occurred
public class ProcessMeetsBatch {

    private Logger logger = LoggerFactory.getLogger(ProcessMeetsBatch.class);

    private GenerateMeetReport meetReporter;
    private ReportPostProcessor postProcessor;
    private MeetRepository meetRepository;

    @Inject
    public ProcessMeetsBatch(GenerateMeetReport meetReporter, ReportPostProcessor postProcessor, MeetRepository meetRepository) {
        this.meetReporter = meetReporter;
        this.postProcessor = postProcessor;
        this.meetRepository = meetRepository;
    }

    public void processMeets() {
        List<Meet> meets = meetRepository.getAllLockedMeetsBetween(Instant.now().minus(14, ChronoUnit.DAYS), Instant.now());
        for (Meet meet : meets) {
            logger.info("Processing meet: " + meet.id);
            MeetProcessingThread job = new MeetProcessingThread(meet, meetReporter, postProcessor);
            ExecuteJobs.scheduler.submit(job);
        }
    }

}
