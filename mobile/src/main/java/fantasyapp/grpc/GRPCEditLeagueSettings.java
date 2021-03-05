package fantasyapp.grpc;

import com.fantasytrack.protos.LeagueService;
import com.fantasytrack.protos.LeaguesGrpc;
import com.google.inject.Inject;
import domain.exception.ApplicationException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.league.EditDraftTime;
import usecase.league.EditLeagueInfo;

// Not a seperate grpc service but used to break up the league service
public class GRPCEditLeagueSettings extends LeaguesGrpc.LeaguesImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCEditLeagueSettings.class);

    private EditDraftTime editDraftTime;
    private EditLeagueInfo editLeagueInfo;

    @Inject
    public GRPCEditLeagueSettings(EditDraftTime editDraftTime, EditLeagueInfo editLeagueInfo) {
        this.editDraftTime = editDraftTime;
        this.editLeagueInfo = editLeagueInfo;
    }

    @Override
    public void editSettings(LeagueService.LeagueSettingsRequest request, StreamObserver<LeagueService.LeagueSettingsResponse> responseObserver) {
        logger.info("Editing league settings");
        try {
            editSettings(request);
            responseObserver.onNext(LeagueService.LeagueSettingsResponse.newBuilder().build());
            responseObserver.onCompleted();
            logger.info("League settings edited successfully");
        } catch (Exception e) {
            logger.info("Error editing league settings");
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    private void editSettings(LeagueService.LeagueSettingsRequest request) throws ApplicationException {
        String leagueId = Authenticator.leagueKey.get();
        editLeagueInfo.verifyCanEdit(leagueId, Authenticator.teamKey.get());
        if (request.hasDraftTime()) {
            editDraftTime.editDraftTime(leagueId, TimeSerializer.deserializeTime(request.getDraftTime()));
        } else if (!request.getName().isEmpty()) {
            editLeagueInfo.editName(leagueId, request.getName());
        } else if (request.getMaxTeams() != 0) {
            editLeagueInfo.editMaxTeams(leagueId, request.getMaxTeams());
        } else if (request.getTurnDuration() != 0) {
            editLeagueInfo.editDraftTurnDuration(leagueId, request.getTurnDuration());
        }
    }


}
