package fantasyapp.draftInstance;

import fantasyapp.draftInstance.grpc.DraftStateUpdater;
import fantasyapp.draftInstance.grpc.SerializedStateListener;
import fantasyapp.draftInstance.grpc.serializer.DraftInfoSerializer;
import fantasyapp.grpc.Authenticator;
import fantasyapp.grpc.GrpcError;
import com.google.protobuf.Empty;
import domain.exception.ApplicationException;
import com.fantasytrack.protos.DraftGrpc;
import com.fantasytrack.protos.DraftService;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.ContractRequest;
import usecase.DraftAthlete;
import usecase.dto.DraftInfoDTO;
import usecase.port.DraftInfoPort;

import javax.inject.Inject;

public class GrpcDraftService extends DraftGrpc.DraftImplBase {

    private Logger logger = LoggerFactory.getLogger(GrpcDraftService.class);

    private DraftStateUpdater draftStateUpdater;
    private DraftInfoPort draftInfoPort;
    private DraftAthlete draftAthlete;

    @Inject
    public GrpcDraftService(DraftStateUpdater draftStateUpdater, DraftInfoPort draftInfoPort, DraftAthlete draftAthlete) {
        this.draftStateUpdater = draftStateUpdater;
        this.draftInfoPort = draftInfoPort;
        this.draftAthlete = draftAthlete;
    }

    @Override
    public void draftingStream(Empty request, StreamObserver<DraftService.DraftState> responseObserver) {
        logger.info("Receiving draft stream request from client");
        ServerCallStreamObserver<DraftService.DraftState> scso = (ServerCallStreamObserver<DraftService.DraftState>) responseObserver;
        SerializedStateListener listener = draftState -> {
            responseObserver.onNext(draftState);
            logger.info("Sending draft state to client");
        };
        draftStateUpdater.registerStateListenerAndGetLatest(listener);
        scso.setOnCancelHandler(() -> draftStateUpdater.unregisterStateListener(listener));
    }

    @Override
    public void draftAthlete(DraftService.DraftRequest request, StreamObserver<Empty> responseObserver) {
        logger.info("Received draft request from client");
        ContractRequest contract = ContractRequest.builder()
                .teamId(Authenticator.teamKey.get())
                .leagueId(Authenticator.leagueKey.get())
                .athleteId(request.getAthleteId())
                .build();

        try {
            draftAthlete.draft(contract);
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
            logger.info("Notifying client that draft pick was successful");
        } catch (ApplicationException e) {
            logger.warn("Warning when attempting to draft: Request: " + contract + " | ", e);
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void getDraftInfo(Empty request, StreamObserver<DraftService.DraftInfo> responseObserver) {
        logger.info("Received request for draft info from client");
        DraftInfoDTO draftInfoDTO = draftInfoPort.getInfo();
        responseObserver.onNext(DraftInfoSerializer.serializeDraftInfo(draftInfoDTO));
        responseObserver.onCompleted();
        logger.info("Sent client draft info");
    }

}
