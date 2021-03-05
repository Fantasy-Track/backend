package fantasyapp.masterDraftServer;

import fantasyapp.draftInstance.GrpcDraftService;
import fantasyapp.grpc.Authenticator;
import fantasyapp.grpc.GrpcError;
import com.google.protobuf.Empty;
import domain.exception.DraftNotRunning;
import com.fantasytrack.protos.DraftGrpc;
import com.fantasytrack.protos.DraftService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GrpcDraftServiceRouter extends DraftGrpc.DraftImplBase {

    private Logger logger = LoggerFactory.getLogger(GrpcDraftServiceRouter.class);
    private Map<String, GrpcDraftService> serverMap = new ConcurrentHashMap<>();

    public void addGrpcService(String leagueId, GrpcDraftService draftService) {
        logger.info("New draft server registered for league: " + leagueId);
        this.serverMap.put(leagueId, draftService);
    }

    public void removeGrpcService(String leagueId) {
        logger.info("Draft server unregistered for league: " + leagueId);
        this.serverMap.remove(leagueId);
    }

    private <Res> String getAndVerifyLeague(StreamObserver<Res> responseObserver) {
        String leagueId = Authenticator.leagueKey.get();
        if (!serverMap.containsKey(leagueId)) {
            responseObserver.onError(GrpcError.makeError(new DraftNotRunning()));
            return null;
        }
        return leagueId;
    }

    private <Res> void forwardMessage(Function<GrpcDraftService, Void> grpcFunction, StreamObserver<Res> responseObserver) {
        String leagueId = getAndVerifyLeague(responseObserver);
        if (leagueId == null) return;
        grpcFunction.apply(serverMap.get(leagueId));
    }

    @Override
    public void draftingStream(Empty request, StreamObserver<DraftService.DraftState> responseObserver) {
        forwardMessage(draftService -> {
            draftService.draftingStream(request, responseObserver);
            return null;
        }, responseObserver);
    }

    @Override
    public void draftAthlete(DraftService.DraftRequest request, StreamObserver<Empty> responseObserver) {
        forwardMessage(draftService -> {
            draftService.draftAthlete(request, responseObserver);
            return null;
        }, responseObserver);

    }

    @Override
    public void getDraftInfo(Empty request, StreamObserver<DraftService.DraftInfo> responseObserver) {
        forwardMessage(draftService -> {
            draftService.getDraftInfo(request, responseObserver);
            return null;
        }, responseObserver);
    }
}
