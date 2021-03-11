package fantasyapp;

import fantasyapp.batchAssignments.ProcessMeetsBatch;
import fantasyapp.batchAssignments.ProcessTradesBatch;
import fantasyapp.grpc.GrpcJobsService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;
import usecase.meetLocking.LockMeets;
import usecase.meetLocking.UpcomingMeetNotifier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.*;
import java.util.TimeZone;

public class ExecuteJobs {

    private final Logger logger = LoggerFactory.getLogger(ExecuteJobs.class);

    public static final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    private ProcessMeetsBatch processMeetsBatch;
    private ProcessTradesBatch processTradesBatch;

    @Inject
    public ExecuteJobs(ProcessMeetsBatch processMeetsBatch, ProcessTradesBatch processTradesBatch) {
        this.processMeetsBatch = processMeetsBatch;
        this.processTradesBatch = processTradesBatch;
    }

    public void execute() {
        logger.info("Running meets processing job");
        processMeetsBatch.processMeets();

        logger.info("Running trades processing job");
        processTradesBatch.processTrades();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new JobsModule());
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        LockMeets lockMeets = injector.getInstance(LockMeets.class);
        UpcomingMeetNotifier upcomingMeetNotifier = injector.getInstance(UpcomingMeetNotifier.class);

        scheduler.setPoolSize(10);
        scheduler.initialize();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                ExecuteJobs executor = injector.getInstance(ExecuteJobs.class);
                executor.execute();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }, Duration.ofMinutes(30));

        scheduler.schedule(lockMeets::lockMeetsToday, new CronTrigger("0 1 0 * * ?", TimeZone.getTimeZone("America/Los_Angeles")));
        scheduler.schedule(upcomingMeetNotifier::sendNotificationForUpcomingMeets, new CronTrigger("0 0 17 * * ?", TimeZone.getTimeZone("America/Los_Angeles")));

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println("Shutting down");
                    scheduler.setWaitForTasksToCompleteOnShutdown(true);
                    scheduler.shutdown();
                    StaticDB.redis.shutdown();
                }
        ));

        Server server = NettyServerBuilder.forPort(EnvVars.PORT)
                .addService(injector.getInstance(GrpcJobsService.class))
                .build();

        server.start();
        server.awaitTermination();
    }

}
