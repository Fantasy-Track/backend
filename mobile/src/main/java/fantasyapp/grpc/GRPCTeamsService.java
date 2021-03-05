package fantasyapp.grpc;

import com.fantasytrack.protos.TeamService;
import com.fantasytrack.protos.TeamsGrpc;
import fantasyapp.grpc.serializer.RosterSerializer;
import fantasyapp.grpc.serializer.TeamSerializer;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import usecase.dto.AthleteOwnershipDTO;
import usecase.team.*;

import java.util.List;

public class GRPCTeamsService extends TeamsGrpc.TeamsImplBase {

    private final GetTeams teamListings;
    private final GetTeamRoster teamRoster;
    private final EditRoster rosterEditor;
    private final DeleteTeam deleteTeam;
    private final EditTeamInfo editTeam;

    @Inject
    public GRPCTeamsService(GetTeams teamListings, GetTeamRoster teamRoster, EditRoster rosterEditor, DeleteTeam deleteTeam, EditTeamInfo editTeam) {
        this.teamListings = teamListings;
        this.teamRoster = teamRoster;
        this.rosterEditor = rosterEditor;
        this.deleteTeam = deleteTeam;
        this.editTeam = editTeam;
    }

    @Override
    public void getTeamRoster(TeamService.GetRosterRequest request, StreamObserver<TeamService.TeamLineup> responseObserver) {
        List<AthleteOwnershipDTO> roster = teamRoster.getRoster(request.getTeamId(), request.getMeetId().isEmpty() ? null : request.getMeetId());
        responseObserver.onNext(RosterSerializer.serializeLineup(roster));
        responseObserver.onCompleted();
    }

    @Override
    public void editRoster(TeamService.EditRosterRequest request, StreamObserver<TeamService.TeamLineup> responseObserver) {
        RosterEditRequest editRequest = RosterEditRequest.builder()
                .athleteId(request.getAthleteId())
                .eventId(request.getEventId())
                .leagueId(Authenticator.leagueKey.get())
                .teamId(Authenticator.teamKey.get())
                .build();

        try {
            rosterEditor.edit(editRequest);
            List<AthleteOwnershipDTO> roster = teamRoster.getRoster(Authenticator.teamKey.get(), null);
            responseObserver.onNext(RosterSerializer.serializeLineup(roster));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void getOwnedTeams(TeamService.OwnedTeamsRequest request, StreamObserver<TeamService.OwnedTeamsResponse> responseObserver) {
        List<TeamDTO> dtos = teamListings.getTeamsOwnedBy(request.getOwnerId());
        responseObserver.onNext(TeamSerializer.serializeOwnedTeams(dtos));
        responseObserver.onCompleted();
    }

    @Override
    public void getTeamListings(TeamService.TeamListingsRequest request, StreamObserver<TeamService.TeamListingsResponse> responseObserver) {
        List<TeamDTO> dtos = teamListings.getTeamsInLeague(request.getLeagueId());
        responseObserver.onNext(TeamSerializer.serializeTeamListings(dtos));
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTeam(TeamService.DeleteTeamRequest request, StreamObserver<Empty> responseObserver) {
        try {
            deleteTeam.deleteTeam(DeleteTeam.DeleteTeamRequest.builder()
                    .actingTeamId(Authenticator.teamKey.get())
                    .teamToDeleteId(request.getId())
                    .leagueId(Authenticator.leagueKey.get())
                    .build());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void editTeamName(TeamService.TeamNameRequest request, StreamObserver<TeamService.TeamInfo> responseObserver) {
        try {
            String teamId = Authenticator.teamKey.get();
            editTeam.editTeamName(teamId, Authenticator.leagueKey.get(), request.getName());

            TeamDTO dto = teamListings.getTeamById(teamId);
            responseObserver.onNext(TeamSerializer.serializeTeamInfo(dto));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void getTeamInfo(TeamService.TeamInfoRequest request, StreamObserver<TeamService.TeamInfo> responseObserver) {
        try {
            TeamDTO dto = teamListings.getTeamById(request.getId());
            responseObserver.onNext(TeamSerializer.serializeTeamInfo(dto));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }
}
