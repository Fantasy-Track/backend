package usecase.team;

import com.google.inject.Inject;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.NameRepository;
import usecase.createTeam.TeamNameValidator;

public class EditTeamInfo {

    private NameRepository nameRepository;
    private TeamNameValidator teamNameValidator;

    @Inject
    public EditTeamInfo(NameRepository nameRepository, TeamNameValidator teamNameValidator) {
        this.nameRepository = nameRepository;
        this.teamNameValidator = teamNameValidator;
    }

    public void editTeamName(String teamId, String leagueId, String teamName) throws NameTooShort, NameTaken, NameTooLong {
        teamNameValidator.validateName(teamName, leagueId);
        nameRepository.updateTeamName(teamId, teamName);
    }

}
