package usecase.assembler;

import domain.entity.Team;
import domain.repository.NameRepository;
import usecase.team.TeamDTO;

public class TeamAssembler {

    public static TeamDTO writeDTO(Team team, NameRepository nameRepository) {
        return TeamDTO.builder()
                .id(team.id)
                .leagueId(team.leagueId)
                .ownerId(team.ownerId)
                .name(team.name)
                .leagueName(nameRepository.getLeagueName(team.leagueId))
                .ownerName(nameRepository.getOwnerName(team.ownerId))
                .fantasyPoints(team.fantasyPoints)
                .build();
    }

}
