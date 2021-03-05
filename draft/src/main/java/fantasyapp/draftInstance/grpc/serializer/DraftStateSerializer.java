package fantasyapp.draftInstance.grpc.serializer;

import com.fantasytrack.protos.DraftService;
import usecase.dto.ContractDTO;
import usecase.dto.DraftStateDTO;

public class DraftStateSerializer {

    public static DraftService.DraftState serializeDraftState(DraftStateDTO dto) {
        DraftService.DraftState.Builder builder = DraftService.DraftState.newBuilder();
        builder.setStatus(DraftStatusSerializer.serializeDraftStatus(dto.getDraftStatusDTO()));

        for (ContractDTO contractDTO : dto.getContractDTOs()) {
            builder.addContracts(ContractSerializer.serializeContract(contractDTO));
        }

        if (dto.getTurnDTO() != null) {
            builder.setTurn(TurnSerializer.serializeTurn(dto.getTurnDTO()));
        }
        return builder.build();
    }

}
