package usecase.createTeam;

import com.google.inject.Inject;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.TeamRepository;

public class TeamNameValidator {

    private TeamRepository teamRepository;

    @Inject
    public TeamNameValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void validateName(String name, String leagueId) throws NameTooShort, NameTooLong, NameTaken {
        if (name.length() < 3) throw new NameTooShort();
        else if (name.length() > 16) throw new NameTooLong();
        if (teamRepository.isNameTaken(name, leagueId)) throw new NameTaken();
    }

    public void validateNameIgnoreTaken(String name) throws NameTooShort, NameTooLong, NameTaken {
        if (name.length() < 3) throw new NameTooShort();
        else if (name.length() > 16) throw new NameTooLong();
    }

}
