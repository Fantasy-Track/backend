package usecase.assembler;

import domain.entity.Result;
import domain.repository.NameRepository;
import usecase.dto.ResultDTO;

public class ResultAssembler {

    public static ResultDTO writeDTO(Result result, NameRepository nameRepository) {
        return ResultDTO.builder()
                .athleteName(nameRepository.getAthleteName(result.athleteId))
                .athleteId(result.athleteId)
                .teamId(result.teamId)
                .teamName(result.teamId == null ? null : nameRepository.getTeamName(result.teamId))
                .eventId(result.eventId)
                .url(result.url)
                .date(result.date)
                .fantasyPoints(result.fantasyPoints)
                .mark(result.mark)
                .id(result.id)
                .meetId(result.meetId)
                .meetName(nameRepository.getMeetName(result.meetId))
                .rank(result.rank)
                .build();
    }

}
