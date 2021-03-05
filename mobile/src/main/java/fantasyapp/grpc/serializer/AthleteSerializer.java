package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.AthleteService;
import com.fantasytrack.protos.Resources;
import usecase.dto.AthleteDTO;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AthleteSerializer {

    public static Resources.Athlete.Builder makeAthleteBuilder(AthleteDTO dto) {
        return Resources.Athlete.newBuilder()
                .setId(dto.id)
                .setName(dto.name)
                .addAllPrimaryEventIds(dto.primaryEvents.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .setGender(dto.gender)
                .putAllProjections(dto.projections);
    }

    public static AthleteService.OwnershipResponse serializeOwnershipResponse(List<AthleteOwnershipDTO> dto) {
        List<Resources.Athlete> athletes = dto.stream().map(AthleteSerializer::serializeAthleteOwnership)
                .collect(Collectors.toList());
        return AthleteService.OwnershipResponse.newBuilder()
                .addAllAthletes(athletes)
                .build();
    }

    public static Resources.Athlete serializeAthleteOwnership(AthleteOwnershipDTO dto) {
        Resources.Athlete.Builder builder = AthleteSerializer.makeAthleteBuilder(dto);
        Resources.OwnershipData.Builder ownership = Resources.OwnershipData.newBuilder();
        ownership.setOwned(dto.owned);
        if (dto.owned) {
            ownership.setTeamId(dto.teamId);
            ownership.setTeamName(dto.teamName);
            ownership.setAssignedPositionId(dto.positionId);
        }
        return builder.setOwnership(ownership).build();
    }
}
