package fantasyapp.grpc;

import com.fantasytrack.protos.LeagueService;
import com.fantasytrack.protos.LeaguesGrpc;
import fantasyapp.grpc.serializer.LeagueSerializer;
import com.google.inject.Inject;
import domain.exception.LeagueNotExists;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.athletes.GetAthletes;
import usecase.dto.LeagueDTO;
import usecase.league.CreateLeague;
import usecase.league.CreateLeagueDTO;
import usecase.league.CreateLeagueRequest;
import usecase.league.GetLeague;

import java.util.Arrays;
import java.util.List;

public class GRPCLeaguesService extends LeaguesGrpc.LeaguesImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCLeaguesService.class);

    private GetAthletes getAthletes;
    private CreateLeague createLeague;
    private GetLeague getLeague;
    private GRPCEditLeagueSettings editLeagueSettings;

    @Inject
    public GRPCLeaguesService(GetAthletes getAthletes, CreateLeague createLeague, GetLeague getLeague, GRPCEditLeagueSettings editLeagueSettings) {
        this.getAthletes = getAthletes;
        this.createLeague = createLeague;
        this.getLeague = getLeague;
        this.editLeagueSettings = editLeagueSettings;
    }

    @Override
    public void getAthletesInLeague(LeagueService.AthletesLeagueRequest request, StreamObserver<LeagueService.LeagueResponse> responseObserver) {
        try {
            List<String> ids = getAthletes.getAthleteIdsInLeague(request.getLeagueId());
            responseObserver.onNext(LeagueSerializer.serializeLeagueResponse(ids));
            responseObserver.onCompleted();
        } catch (LeagueNotExists leagueNotExists) {
            responseObserver.onError(GrpcError.makeError(leagueNotExists));
        }
    }

    @Override
    public void getLeagueInfo(LeagueService.LeagueInfoRequest request, StreamObserver<LeagueService.LeagueInfoResponse> responseObserver) {
        try {
            LeagueDTO dto = getLeague.getLeagueInfo(request.getLeagueId());
            responseObserver.onNext(LeagueSerializer.serializeLeague(dto));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void createLeague(LeagueService.CreateLeagueRequest request, StreamObserver<LeagueService.CreateLeagueResponse> responseObserver) {
        long lastTime = System.currentTimeMillis();
        try {
            logger.info("Creating league: " + request);
            CreateLeagueDTO response = createLeague.createLeague(CreateLeagueRequest.builder()
                    .schoolId(request.getSchoolId())
                    .name(request.getLeagueName())
                    .ownerId(Authenticator.ownerKey.get())
                    .teamName(request.getTeamName())
                    .draftTime(TimeSerializer.deserializeTime(request.getDraftTime()))
                    .build());
            responseObserver.onNext(LeagueSerializer.serializeCreateLeagueResponse(response));
            responseObserver.onCompleted();
            logger.info("League created successfully");
        } catch (Exception e) {
            logger.info("Error creating league: " + Arrays.toString(e.getStackTrace()));
            responseObserver.onError(GrpcError.makeError(e));
        }
        System.out.println("Creating league took ms: " + (System.currentTimeMillis() - lastTime));
    }

    @Override
    public void editSettings(LeagueService.LeagueSettingsRequest request, StreamObserver<LeagueService.LeagueSettingsResponse> responseObserver) {
        editLeagueSettings.editSettings(request, responseObserver);
    }

}
