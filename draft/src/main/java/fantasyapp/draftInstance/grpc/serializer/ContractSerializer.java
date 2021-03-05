package fantasyapp.draftInstance.grpc.serializer;

import fantasyapp.grpc.TimeSerializer;
import com.fantasytrack.protos.DraftService;
import usecase.dto.ContractDTO;

public class ContractSerializer {

    static DraftService.Contract serializeContract(ContractDTO dto) {
        DraftService.Contract.Builder builder = DraftService.Contract.newBuilder();
        builder.setAthleteId(dto.athleteId);
        builder.setTeamId(dto.teamId);
        builder.setTeamName(dto.teamName);
        builder.setAthleteName(dto.athleteName);
        builder.setDateSigned(TimeSerializer.serializeTime(dto.dateSigned));
        return builder.build();
    }

}
