package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.MeetService;
import fantasyapp.grpc.TimeSerializer;
import usecase.dto.MeetDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MeetSerializer {

    public static MeetService.GetMeetsResponse serializeMeets(List<MeetDTO> dtoList) {
        List<MeetService.MeetInfo> infoList = dtoList.stream().map(meetDTO -> MeetService.MeetInfo.newBuilder()
                .setId(meetDTO.id)
                .setName(meetDTO.name)
                .setDate(TimeSerializer.serializeTime(meetDTO.date))
                .setHasResults(meetDTO.hasResults)
                .setEnabled(meetDTO.enabled)
                .setLocked(meetDTO.locked)
                .setLeagueId(meetDTO.leagueId)
                .setAthleticId(meetDTO.athleticId)
                .build()).collect(Collectors.toList());
        return MeetService.GetMeetsResponse.newBuilder()
                .addAllMeets(infoList)
                .build();
    }

}
