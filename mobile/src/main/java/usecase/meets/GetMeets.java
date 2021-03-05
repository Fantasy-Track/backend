package usecase.meets;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.repository.MeetRepository;
import usecase.assembler.MeetAssembler;
import usecase.dto.MeetDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GetMeets {

    private MeetRepository meetRepository;

    @Inject
    public GetMeets(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    public List<MeetDTO> getMeetsInLeague(String leagueId) {
        List<Meet> meets = meetRepository.getMeetsInLeague(leagueId);
        return meets.stream().map(MeetAssembler::writeDTO).collect(Collectors.toList());
    }

}
