package usecase.createTeam;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.*;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;

public class JoinLeagueValidator {

    private TeamRepository teamRepository;
    private LeagueRepository leagueRepository;

    @Inject
    public JoinLeagueValidator(TeamRepository teamRepository, LeagueRepository leagueRepository) {
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
    }

    public void validateOwnerJoiningLeague(String ownerId, String leagueId) throws ApplicationException {
        if (teamRepository.doesOwnerHaveTeamInLeague(ownerId, leagueId)){
            throw new AlreadyInLeague();
        }
        verifyLeagueValid(leagueId);
    }

    private void verifyLeagueValid(String leagueId) throws LeagueNotExists, AlreadyDrafted, LeagueFull {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) {
            throw new LeagueNotExists();
        } else if (league.status != LeagueStatus.PRE_DRAFT) {
            throw new AlreadyDrafted();
        } else if (teamRepository.countTeamsInLeague(leagueId) >= league.maxTeams) {
            throw new LeagueFull();
        }
    }

}
