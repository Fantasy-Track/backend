package fantasyapp.job;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.entity.ScoredResult;
import domain.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.meetProcessing.GenerateMeetReport;
import usecase.postProcess.ReportPostProcessor;

import java.util.List;

public class MeetProcessingThread extends Thread {

    private Meet meet;

    private GenerateMeetReport meetReporter;
    private ReportPostProcessor postProcessor;

    private Logger logger = LoggerFactory.getLogger(MeetProcessingThread.class);

    @Inject
    public MeetProcessingThread(Meet meet, GenerateMeetReport meetReporter, ReportPostProcessor postProcessor) {
        this.meet = meet;
        this.meetReporter = meetReporter;
        this.postProcessor = postProcessor;
    }

    @Inject
    public void run() {
        try {
            List<ScoredResult> results = meetReporter.generate(meet);
            postProcessor.postProcess(results, meet);
        } catch (ApplicationException e) { // also fires when there are no results
            logger.warn("Meet: " + meet.toString(), e);
        } catch (Exception e) {
            logger.error("Error when processing meet: " + meet.toString(), e);
        }
    }

}
