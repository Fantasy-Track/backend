package usecase.team;

import com.google.inject.Inject;
import domain.entity.Team;
import domain.exception.TeamNotExists;
import domain.repository.NameRepository;
import domain.repository.TeamRepository;
import usecase.assembler.TeamAssembler;

import java.util.List;
import java.util.stream.Collectors;

public class GetTeams {

    private TeamRepository teamRepository;
    private NameRepository nameRepository;

    @Inject
    public GetTeams(TeamRepository teamRepository, NameRepository nameRepository) {
        this.teamRepository = teamRepository;
        this.nameRepository = nameRepository;
    }

    public List<TeamDTO> getTeamsOwnedBy(String ownerId) {
        List<Team> teams = teamRepository.getOwnedTeams(ownerId);
        return teamsToDTOList(teams);
    }

    public List<TeamDTO> getTeamsInLeague(String leagueId) {
        List<Team> teams = teamRepository.getTeamsInLeague(leagueId);
        return teamsToDTOList(teams);
    }

    public TeamDTO getTeamById(String id) throws TeamNotExists {
        Team team = teamRepository.getTeamById(id);
        if (team == null) throw new TeamNotExists();
        return TeamAssembler.writeDTO(team, nameRepository);
    }

    private List<TeamDTO> teamsToDTOList(List<Team> teams) {
        return teams.stream().map((Team team) -> TeamAssembler.writeDTO(team, nameRepository))
                .collect(Collectors.toList());
    }

}
