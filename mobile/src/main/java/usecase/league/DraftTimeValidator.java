package usecase.league;

import domain.exception.DraftTimeTooEarly;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DraftTimeValidator {

    public void validateDraftTime(Instant draftTime) throws DraftTimeTooEarly {
        if (draftTime.minus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            throw new DraftTimeTooEarly();
        }
    }

}
