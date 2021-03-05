package fantasyapp.draftInstance.grpc.serializer;

import fantasyapp.grpc.TimeSerializer;
import com.fantasytrack.protos.DraftService;
import usecase.dto.TurnDTO;

public class TurnSerializer {

    static public DraftService.Turn serializeTurn(TurnDTO dto) {
        DraftService.Turn.Builder builder = DraftService.Turn.newBuilder();
        builder.setTeamId(dto.getTeamId());
        builder.setTeamName(dto.getTeamName());
        builder.setEndTime(TimeSerializer.serializeTime(dto.getEndTime()));
        builder.setRound(dto.getRound());
        builder.setPick(dto.getPick());
        return builder.build();
    }

}
