package usecase.league;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.*;
import domain.repository.EditLeagueRepository;
import domain.repository.NameRepository;
import domain.repository.TeamRepository;

public class EditLeagueInfo {

    private EditLeagueRepository leagueRepository;
    private LeagueNameValidator validator;
    private TeamRepository teamRepository;
    private NameRepository nameRepository;

    @Inject
    public EditLeagueInfo(EditLeagueRepository leagueRepository, LeagueNameValidator validator, TeamRepository teamRepository, NameRepository nameRepository) {
        this.leagueRepository = leagueRepository;
        this.validator = validator;
        this.teamRepository = teamRepository;
        this.nameRepository = nameRepository;
    }

    public void verifyCanEdit(String leagueId, String teamId) throws LeagueNotExists, UnauthorizedException {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) throw new LeagueNotExists();
        else if (!league.owningTeam.equals(teamId)) throw new UnauthorizedException();
    }

    public void editName(String leagueId, String name) throws NameTooShort, NameTooLong {
        validator.validateName(name);
        nameRepository.updateLeagueName(leagueId, name);
    }

    public void editMaxTeams(String leagueId, int maxTeams) throws CannotReduceMaxTeams, LeagueNotEnoughAthletes {
        League league = leagueRepository.getLeagueById(leagueId);
        int newMaxTeams = Math.min(league.athletes.size() / league.leagueSettings.teamSize, Math.max(maxTeams, teamRepository.countTeamsInLeague(leagueId)));
        leagueRepository.updateMaxTeams(leagueId, newMaxTeams);
        if (newMaxTeams < maxTeams) throw new LeagueNotEnoughAthletes();
        else if (newMaxTeams > maxTeams) throw new CannotReduceMaxTeams();
    }

    public void editDraftTurnDuration(String leagueId, int seconds) throws CannotDoActionNow, MustBeBetween {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league.status != LeagueStatus.PRE_DRAFT) throw new CannotDoActionNow();
        if (25 > seconds || seconds > 180) throw new MustBeBetween(25, 180, "seconds");
        leagueRepository.setTurnDurationMillis(leagueId,seconds * 1000);
    }

}
