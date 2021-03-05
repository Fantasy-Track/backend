package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.LeagueService;
import fantasyapp.grpc.TimeSerializer;
import domain.entity.LeagueStatus;
import usecase.dto.LeagueDTO;
import usecase.league.CreateLeagueDTO;

import java.util.List;

public class LeagueSerializer {

    public static LeagueService.LeagueResponse serializeLeagueResponse(List<String> athletesIds) {
        return LeagueService.LeagueResponse.newBuilder()
                .addAllAthletes(athletesIds)
                .build();
    }

    public static LeagueService.CreateLeagueResponse serializeCreateLeagueResponse(CreateLeagueDTO response) {
        return LeagueService.CreateLeagueResponse.newBuilder()
                .setLeagueId(response.leagueId)
                .setOwningTeamId(response.owningTeamId).build();
    }

    public static LeagueService.LeagueInfoResponse serializeLeague(LeagueDTO dto) {
        return LeagueService.LeagueInfoResponse.newBuilder()
                .setId(dto.id)
                .setName(dto.name)
                .setMaxTeams(dto.maxTeams)
                .setNumTeams(dto.numTeams)
                .setOwningTeamId(dto.owningTeamId)
                .setStatus(serializeLeagueStatus(dto.status))
                .setDraftTime(TimeSerializer.serializeTime(dto.draftTime))
                .setIpAddress(dto.draftIPAddress != null ? dto.draftIPAddress : "")
                .setTurnDuration(dto.turnDuration / 1000)
                .build();
    }

    private static LeagueService.LeagueInfoResponse.LeagueStatus serializeLeagueStatus(LeagueStatus status) {
        switch (status) {
            case DRAFTING:
                return LeagueService.LeagueInfoResponse.LeagueStatus.DRAFTING;
            case POST_DRAFT:
                return LeagueService.LeagueInfoResponse.LeagueStatus.POST_DRAFT;
            case PRE_DRAFT:
            default:
                return LeagueService.LeagueInfoResponse.LeagueStatus.PRE_DRAFT;
        }
    }

}
