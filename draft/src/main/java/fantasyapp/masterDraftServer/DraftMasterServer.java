package fantasyapp.masterDraftServer;

import fantasyapp.EnvVars;
import fantasyapp.SharedEnvVars;
import fantasyapp.draftInstance.GrpcDraftService;
import fantasyapp.draftInstance.SingleInstanceDraftServer;
import fantasyapp.grpc.Authenticator;
import com.google.inject.Inject;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.notification.NotificationFactory;

import java.net.InetSocketAddress;

public class DraftMasterServer extends Thread {

    private Logger logger = LoggerFactory.getLogger(DraftMasterServer.class);

    private GrpcDraftServiceRouter router;

    private DraftRegistrar registrar;
    private Authenticator authenticator;

    @Inject
    public DraftMasterServer(DraftRegistrar registrar, Authenticator authenticator) {
        this.registrar = registrar;
        this.authenticator = authenticator;
    }

    @SneakyThrows
    @Override
    public void run() {
        router = new GrpcDraftServiceRouter();

        Server server = ServerBuilder.forPort(EnvVars.PORT)
                .addService(router)
                .intercept(authenticator)
                .build();
        server.start();
        server.awaitTermination();
    }

    public void startDraftServerForLeague(String leagueId) {
        new Thread(() -> {
            logger.info("Starting draft room for league: " + leagueId);
            SingleInstanceDraftServer draftInstance = new SingleInstanceDraftServer(leagueId);
            GrpcDraftService draftService = draftInstance.createGrpcService();

            router.addGrpcService(leagueId, draftService);
            registrar.registerDraft(leagueId, ""); // should not be registered until all the draft info is loaded and setup
            NotificationFactory.handler.sendDraftingSoon(leagueId);

            try {
                draftInstance.startAndBlock();
            } catch (InterruptedException e) {
                logger.error("Error running draft thread", e);
            }

            // keep server open for an extra 10 minutes
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                logger.error("Error keeping draft open after finished", e);
            }

            registrar.unregisterDraft(leagueId);
            router.removeGrpcService(leagueId);
            logger.info("Closing draft room for league: " + leagueId);
        }).start();
    }

}
