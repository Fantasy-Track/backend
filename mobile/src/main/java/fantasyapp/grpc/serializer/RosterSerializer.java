package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.Resources;
import com.fantasytrack.protos.TeamService;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;
import java.util.stream.Collectors;

public class RosterSerializer {

    public static TeamService.TeamLineup serializeLineup(List<AthleteOwnershipDTO> dtoList) {
        List<Resources.Athlete> athletes = dtoList.stream().map(AthleteSerializer::serializeAthleteOwnership).collect(Collectors.toList());
        return TeamService.TeamLineup.newBuilder()
                .addAllAthletes(athletes)
                .build();
    }

}
