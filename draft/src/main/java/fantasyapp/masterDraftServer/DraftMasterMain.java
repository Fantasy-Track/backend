package fantasyapp.masterDraftServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import domain.repository.ScheduledDraftRepository;
import org.slf4j.LoggerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;
import util.DistributedLock;

import java.io.IOException;

public class DraftMasterMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        LoggerFactory.getLogger(DraftMasterMain.class).info("\n/////////////////////////////\n////DRAFT SERVER STARTED////\n/////////////////////////////");
        System.setProperty("com.google.inject.internal.cglib.$experimental_asm7", "true");

        Injector injector = Guice.createInjector(new RouterModule());

        DraftMasterServer server = injector.getInstance(DraftMasterServer.class);
        server.start();

//        server.startDraftServerForLeague("CsT32z"); //temp

        DraftScheduler scheduler = new DraftScheduler(injector.getInstance(ScheduledDraftRepository.class), server, injector.getInstance(DistributedLock.class));
        scheduler.startScheduling();
    }

}
