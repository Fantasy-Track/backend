package fantasyapp.draftInstance.grpc.serializer;

import fantasyapp.grpc.TimeSerializer;
import com.fantasytrack.protos.DraftService;
import usecase.dto.DraftInfoDTO;

public class DraftInfoSerializer {

    public static DraftService.DraftInfo serializeDraftInfo(DraftInfoDTO dto) {
        DraftService.DraftInfo.Builder builder = DraftService.DraftInfo.newBuilder();
        builder.addAllPickingOrder(dto.getPickingOrder());
        builder.setRounds(dto.getRounds());
        builder.setStartTime(TimeSerializer.serializeTime(dto.getStartTime()));
        return builder.build();
    }

}
