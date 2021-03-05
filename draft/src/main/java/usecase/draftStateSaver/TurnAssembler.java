package usecase.draftStateSaver;

import com.google.inject.Inject;
import domain.entity.Turn;
import domain.repository.NameRepository;
import usecase.dto.TurnDTO;

import java.time.Instant;

public class TurnAssembler {

    private NameRepository nameRepository;

    @Inject
    public TurnAssembler(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public TurnDTO writeDTO(Turn turn) {
        TurnDTO dto = new TurnDTO();
        dto.setTeamId(turn.teamId);
        dto.setTeamName(nameRepository.getTeamName(turn.teamId));
        dto.setEndTime(Instant.now().plusMillis(turn.getTimeLeftMillis()));
        dto.setRound(turn.position.round);
        dto.setPick(turn.position.pick);
        return dto;
    }


}
