package fantasyapp.grpc;

import com.fantasytrack.protos.ResultsGrpc;
import com.fantasytrack.protos.ResultsService;
import fantasyapp.grpc.serializer.ResultSerializer;
import com.google.inject.Inject;
import domain.exception.TeamNotExists;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.dto.ResultDTO;
import usecase.results.GetPoints;
import usecase.results.GetResults;

import java.util.List;

public class GRPCResultService extends ResultsGrpc.ResultsImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCResultService.class);

    private GetResults resultsFetcher;
    private GetPoints pointsFetcher;

    @Inject
    public GRPCResultService(GetResults resultsFetcher, GetPoints pointsFetcher) {
        this.resultsFetcher = resultsFetcher;
        this.pointsFetcher = pointsFetcher;
    }

    @Override
    public void getTeamResultsAtMeet(ResultsService.TeamResultsAtMeetRequest request, StreamObserver<ResultsService.ResultsResponse> responseObserver) {
        List<ResultDTO> dto = resultsFetcher.getTeamResultsAtMeet(request.getTeamId(), request.getMeetId());
        responseObserver.onNext(ResultSerializer.serializeResults(dto));
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamPoints(ResultsService.TeamPointsRequest request, StreamObserver<ResultsService.TeamPointsResponse> responseObserver) {

        try {
            double points = pointsFetcher.getPoints(request.getTeamId());
            responseObserver.onNext(ResultsService.TeamPointsResponse.newBuilder().setPoints(points).build());
            responseObserver.onCompleted();
        } catch (TeamNotExists teamNotExists) {
            responseObserver.onError(GrpcError.makeError(teamNotExists));
        }

    }

    @Override
    public void getMeetResults(ResultsService.MeetResultsRequest request, StreamObserver<ResultsService.ResultsResponse> responseObserver) {
        logger.info("Getting meet results");
        List<ResultDTO> dto = resultsFetcher.getMeetResults(request.getMeetId());
        responseObserver.onNext(ResultSerializer.serializeResults(dto));
        responseObserver.onCompleted();
        logger.info("Sent meet results");
    }

    @Override
    public void getAthleteResults(ResultsService.AthleteResultsRequest request, StreamObserver<ResultsService.ResultsResponse> responseObserver) {
        List<ResultDTO> dto = resultsFetcher.getAthleteResults(request.getAthleteId(), request.getLeagueId());
        responseObserver.onNext(ResultSerializer.serializeResults(dto));
        responseObserver.onCompleted();
    }
}
