package fantasyapp.grpc;

import com.fantasytrack.protos.MeetGrpc;
import com.fantasytrack.protos.MeetService;
import fantasyapp.grpc.serializer.MeetSerializer;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import usecase.dto.MeetDTO;
import usecase.meets.EditMeets;
import usecase.meets.EditMeetsRequest;
import usecase.meets.GetMeets;
import usecase.meets.PullMeets;

import java.util.List;

public class GRPCMeetService extends MeetGrpc.MeetImplBase {

    private EditMeets meetEditor;
    private GetMeets meetFetcher;
    private PullMeets pullMeets;

    @Inject
    public GRPCMeetService(EditMeets meetEditor, GetMeets meetFetcher, PullMeets pullMeets) {
        this.meetEditor = meetEditor;
        this.meetFetcher = meetFetcher;
        this.pullMeets = pullMeets;
    }

    @Override
    public void getAllMeets(MeetService.GetMeetsRequest request, StreamObserver<MeetService.GetMeetsResponse> responseObserver) {
        List<MeetDTO> dtos = meetFetcher.getMeetsInLeague(request.getLeagueId());
        responseObserver.onNext(MeetSerializer.serializeMeets(dtos));
        responseObserver.onCompleted();
    }

    @Override
    public void changeEnabledMeets(MeetService.ChangeEnabledMeetsRequest request, StreamObserver<MeetService.GetMeetsResponse> responseObserver) {
        String leagueId = Authenticator.leagueKey.get();
        try {
            meetEditor.editEnabledMeets(EditMeetsRequest.builder()
                    .enableMeetIds(request.getEnabledMeetIdsList())
                    .disableMeetIds(request.getDisabledMeetIdsList())
                    .teamId(Authenticator.teamKey.get())
                    .leagueId(leagueId)
                    .build());
            List<MeetDTO> dtos = meetFetcher.getMeetsInLeague(leagueId);
            responseObserver.onNext(MeetSerializer.serializeMeets(dtos));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void pullMeetSchedule(Empty request, StreamObserver<MeetService.GetMeetsResponse> responseObserver) {
        String leagueId = Authenticator.leagueKey.get();
        try {
            pullMeets.pullMeets(Authenticator.teamKey.get(), leagueId);
            List<MeetDTO> dtos = meetFetcher.getMeetsInLeague(leagueId);
            responseObserver.onNext(MeetSerializer.serializeMeets(dtos));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }
}
