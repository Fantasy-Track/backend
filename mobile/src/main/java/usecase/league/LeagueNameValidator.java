package usecase.league;

import com.google.inject.Inject;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.LeagueRepository;

public class LeagueNameValidator {

    public void validateName(String name) throws NameTooShort, NameTooLong {
        if (name.length() < 3) throw new NameTooShort();
        else if (name.length() > 18) throw new NameTooLong();
    }

}
