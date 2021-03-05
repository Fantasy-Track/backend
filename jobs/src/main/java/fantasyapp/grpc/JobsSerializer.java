package fantasyapp.grpc;

import com.fantasytrack.protos.JobsService;
import usecase.leagueCreation.IndexedAthletesDTO;

public class JobsSerializer {

    public static JobsService.IndexAthletesResponse serializeIndexResponse(IndexedAthletesDTO dto) {
        return JobsService.IndexAthletesResponse.newBuilder()
                .addAllAthleteIds(dto.athletes)
                .build();
    }

}
