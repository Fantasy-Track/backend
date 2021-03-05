package fantasyapp;

import fantasyapp.grpc.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.io.IOException;

public class MobileServer {

    private Logger logger = LoggerFactory.getLogger(MobileServer.class);

    public void run() throws IOException, InterruptedException {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        Injector injector = Guice.createInjector(new MobileModule(System.getenv("JOBS_IP"), Integer.parseInt(System.getenv("JOBS_PORT"))));

        GRPCAthletesService athleteService = injector.getInstance(GRPCAthletesService.class);
        GRPCRegistrationService registrationService = injector.getInstance(GRPCRegistrationService.class);
        GRPCTeamsService teamService = injector.getInstance(GRPCTeamsService.class);
        GRPCMeetService meetService = injector.getInstance(GRPCMeetService.class);
        GRPCResultService resultService = injector.getInstance(GRPCResultService.class);
        GRPCLeaguesService leaguesService = injector.getInstance(GRPCLeaguesService.class);
        GRPCTransactionService transactionService = injector.getInstance(GRPCTransactionService.class);
        GRPCOwnerService ownerService = injector.getInstance(GRPCOwnerService.class);

        Server server = ServerBuilder.forPort(Integer.parseInt(System.getenv("PORT")))
//                .useTransportSecurity(
//                        getClass().getClassLoader().getResourceAsStream("fantasytrack.cer"),
//                        getClass().getClassLoader().getResourceAsStream("fantasytrack.key"))
                .addService(athleteService)
                .addService(meetService)
                .addService(registrationService)
                .addService(teamService)
                .addService(ownerService)
                .addService(resultService)
                .addService(leaguesService)
                .addService(transactionService)
                .intercept(injector.getInstance(Authenticator.class))
                .build();

        logger.info("Mobile server started");

        server.start();
        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MobileServer server = new MobileServer();
        server.run();
    }

}
