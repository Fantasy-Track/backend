package usecase.league;

import com.google.inject.Inject;
import domain.exception.ApplicationException;
import domain.exception.SchoolNotEnoughAthletes;
import usecase.createTeam.TeamNameValidator;

import java.util.List;
import java.util.Set;

public class LeagueValidator {

    private LeagueNameValidator nameValidator;
    private TeamNameValidator teamNameValidator;
    private DraftTimeValidator draftTimeValidator;

    @Inject
    public LeagueValidator(LeagueNameValidator nameValidator, TeamNameValidator teamNameValidator, DraftTimeValidator draftTimeValidator) {
        this.nameValidator = nameValidator;
        this.teamNameValidator = teamNameValidator;
        this.draftTimeValidator = draftTimeValidator;
    }

    public void validateLeagueSetup(CreateLeagueRequest request) throws ApplicationException {
        nameValidator.validateName(request.name);
        teamNameValidator.validateNameIgnoreTaken(request.teamName);
        draftTimeValidator.validateDraftTime(request.draftTime);
    }

    public void validateAthletesInLeague(List<String> athleteIds, int teamSize) throws SchoolNotEnoughAthletes {
        if (athleteIds.size() < teamSize * 2) {
            throw new SchoolNotEnoughAthletes();
        }
    }

}
