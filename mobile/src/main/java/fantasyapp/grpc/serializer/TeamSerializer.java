package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.TeamService;
import usecase.team.TeamDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TeamSerializer {

    public static TeamService.OwnedTeamsResponse serializeOwnedTeams(List<TeamDTO> dtoList) {
        List<TeamService.TeamInfo> infoList = dtoList.stream().map(TeamSerializer::serializeTeamInfo).collect(Collectors.toList());
        return TeamService.OwnedTeamsResponse.newBuilder()
                .addAllTeams(infoList)
                .build();
    }

    public static TeamService.TeamInfo serializeTeamInfo(TeamDTO dto) {
        return TeamService.TeamInfo.newBuilder()
                .setId(dto.id)
                .setLeagueId(dto.leagueId)
                .setLeagueName(dto.leagueName)
                .setName(dto.name)
                .setFantasyPoints(dto.fantasyPoints)
                .setOwnerName(dto.ownerName)
                .build();
    }

    public static TeamService.TeamListingsResponse serializeTeamListings(List<TeamDTO> dtoList) {
        List<TeamService.TeamInfo> infoList = dtoList.stream().map(TeamSerializer::serializeTeamInfo).collect(Collectors.toList());
        return TeamService.TeamListingsResponse.newBuilder()
                .addAllTeams(infoList)
                .build();
    }
}
