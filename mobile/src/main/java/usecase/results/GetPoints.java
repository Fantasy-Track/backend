package usecase.results;

import com.google.inject.Inject;
import domain.entity.Team;
import domain.exception.TeamNotExists;
import domain.repository.TeamRepository;

public class GetPoints {

    private TeamRepository teamRepository;

    @Inject
    public GetPoints(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public double getPoints(String teamId) throws TeamNotExists {
        Team team = teamRepository.getTeamById(teamId);
        if (team == null) throw new TeamNotExists();
        return team.fantasyPoints;
    }

}
