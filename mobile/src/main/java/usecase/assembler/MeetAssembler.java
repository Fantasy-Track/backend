package usecase.assembler;

import domain.entity.Meet;
import usecase.dto.MeetDTO;

public class MeetAssembler {

    public static MeetDTO writeDTO(Meet meet) {
        return MeetDTO.builder()
                .id(meet.id)
                .athleticId(meet.athleticId)
                .leagueId(meet.leagueId)
                .date(meet.date)
                .name(meet.name)
                .enabled(meet.enabled)
                .hasResults(meet.hasResults)
                .locked(meet.locked)
                .build();
    }

}
