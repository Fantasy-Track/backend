package fantasyapp.grpc;

import com.fantasytrack.protos.JobsGrpc;
import com.fantasytrack.protos.JobsService;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import domain.exception.MeetNotScored;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.fantasyPoints.PointsUpdater;
import usecase.leagueCreation.IndexMeets;
import usecase.leagueCreation.IndexedAthletesDTO;
import usecase.leagueCreation.SchoolAthletesIndexer;
import usecase.meetProcessing.RescoreMeet;

import java.util.Arrays;

public class GrpcJobsService extends JobsGrpc.JobsImplBase {

    private Logger logger = LoggerFactory.getLogger(GrpcJobsService.class);

    private SchoolAthletesIndexer schoolAthletesIndexer;
    private IndexMeets meetsIndexer;
    private PointsUpdater pointsUpdater;
    private RescoreMeet rescoreMeet;

    @Inject
    public GrpcJobsService(SchoolAthletesIndexer schoolAthletesIndexer, IndexMeets meetsIndexer, PointsUpdater pointsUpdater, RescoreMeet rescoreMeet) {
        this.schoolAthletesIndexer = schoolAthletesIndexer;
        this.meetsIndexer = meetsIndexer;
        this.pointsUpdater = pointsUpdater;
        this.rescoreMeet = rescoreMeet;
    }

    @Override
    public void indexAthletesInSchool(JobsService.IndexAthletesRequest request, StreamObserver<JobsService.IndexAthletesResponse> responseObserver) {
        try {
            logger.info("Received request to index athletes");
            IndexedAthletesDTO dto = schoolAthletesIndexer.indexAthletesInSchool(request.getSchoolId().trim());
            responseObserver.onNext(JobsSerializer.serializeIndexResponse(dto));
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error("Error when indexing athletes: " + Arrays.toString(e.getStackTrace()));
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void indexMeets(JobsService.IndexMeetsRequest request, StreamObserver<JobsService.IndexMeetsResponse> responseObserver) {
        try {
            meetsIndexer.indexAndUploadMeets(request.getSchoolId(), request.getLeagueId(), TimeSerializer.deserializeTime(request.getDraftTime()));
            responseObserver.onNext(JobsService.IndexMeetsResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.error("Error when indexing meets: ", e);
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void updatePoints(JobsService.UpdatePointsRequest request, StreamObserver<Empty> responseObserver) {
        pointsUpdater.recalculatePointsForLeague(request.getLeagueId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void rescoreMeet(JobsService.RescoreMeetRequest request, StreamObserver<Empty> responseObserver) {
        rescoreMeet.rescoreMeet(request.getMeetId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
