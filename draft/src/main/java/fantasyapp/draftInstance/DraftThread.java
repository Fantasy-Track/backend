package fantasyapp.draftInstance;

import com.google.inject.Inject;
import domain.exception.ApplicationException;
import domain.repository.SettingsRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.DraftSetupTeardown;
import usecase.DraftingProgram;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class DraftThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(DraftThread.class);

    private DraftingProgram loopThread;
    private DraftSetupTeardown draftSetupTeardown;
    private SettingsRepository settingsRepository;

    @Inject
    public DraftThread(DraftingProgram loopThread, DraftSetupTeardown draftSetupTeardown, SettingsRepository settingsRepository) {
        this.loopThread = loopThread;
        this.draftSetupTeardown = draftSetupTeardown;
        this.settingsRepository = settingsRepository;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            logger.error("Draft thread encountered error", e);
        }
    }

    private void execute() throws ApplicationException, InterruptedException {
        draftSetupTeardown.setup();

        CountDownLatch latch = new CountDownLatch(1);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loopThread.loop();
                latch.countDown();
            }
        }, Date.from(settingsRepository.getStartTime()));
        latch.await();

        draftSetupTeardown.teardown();
    }

}
