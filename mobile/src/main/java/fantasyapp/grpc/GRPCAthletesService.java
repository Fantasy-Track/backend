package fantasyapp.grpc;

import com.fantasytrack.protos.AthleteService;
import com.fantasytrack.protos.AthletesGrpc;
import fantasyapp.grpc.serializer.AthleteSerializer;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.athletes.GetAthleteOwnership;
import usecase.dto.AthleteOwnershipDTO;
import usecase.league.PullAthletes;

import java.util.List;

public class GRPCAthletesService extends AthletesGrpc.AthletesImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCAthletesService.class);

    private final GetAthleteOwnership ownership;
    private final PullAthletes pullAthletes;

    @Inject
    public GRPCAthletesService(GetAthleteOwnership ownership, PullAthletes pullAthletes) {
        this.ownership = ownership;
        this.pullAthletes = pullAthletes;
    }

    @Override
    public void getAthleteOwnership(AthleteService.OwnershipRequest request, StreamObserver<AthleteService.OwnershipResponse> responseObserver) {
        List<AthleteOwnershipDTO> dtos = ownership.getAthleteOwnership(request.getAthleteIdsList(), request.getLeagueId());
        responseObserver.onNext(AthleteSerializer.serializeOwnershipResponse(dtos));
        responseObserver.onCompleted();
    }

    @Override
    public void pullAthletes(Empty request, StreamObserver<Empty> responseObserver) {
        logger.info("Pulling athletes for league: " + Authenticator.leagueKey.get());
        try {
            pullAthletes.pullAthletesForLeague(Authenticator.teamKey.get(), Authenticator.leagueKey.get());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
            logger.info("Athletes pulled successfully");
        } catch (Exception e) {
            logger.error("Failed to pull athletes", e);
            responseObserver.onError(GrpcError.makeError(e));
        }
    }
}
