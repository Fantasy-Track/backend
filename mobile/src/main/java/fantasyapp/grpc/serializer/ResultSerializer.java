package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.ResultsService;
import fantasyapp.grpc.TimeSerializer;
import usecase.dto.ResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ResultSerializer {

    public static ResultsService.ResultsResponse serializeResults(List<ResultDTO> resultDTOS) {
        List<ResultsService.Result> results = resultDTOS.stream().map(resultDTO -> ResultsService.Result.newBuilder()
                .setAthleteId(resultDTO.athleteId)
                .setPoints(resultDTO.fantasyPoints)
                .setAthleteName(resultDTO.athleteName)
                .setEventId(resultDTO.eventId)
                .setMeetId(resultDTO.meetId)
                .setMeetName(resultDTO.meetName)
                .setRank(resultDTO.rank)
                .setMark(resultDTO.mark)
                .setTeamId(resultDTO.teamId == null ? "" : resultDTO.teamId)
                .setTeamName(resultDTO.teamName == null ? "" : resultDTO.teamName)
                .setDate(TimeSerializer.serializeTime(resultDTO.date))
                .build()).collect(Collectors.toList());
        return ResultsService.ResultsResponse.newBuilder()
                .addAllResults(results)
                .build();
    }

}
