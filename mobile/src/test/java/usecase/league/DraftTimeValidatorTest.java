package usecase.league;

import domain.exception.DraftTimeTooEarly;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DraftTimeValidatorTest {

    private DraftTimeValidator draftTimeValidator;

    @BeforeMethod
    public void setUp() {
        draftTimeValidator = new DraftTimeValidator();
    }

    @Test
    public void testDraftTimeTooEarly() {
        try {
            draftTimeValidator.validateDraftTime(Instant.now().plus(5, ChronoUnit.MINUTES));
        } catch (DraftTimeTooEarly ignored) {}
    }

    @Test
    public void testValidateDraftTime() throws DraftTimeTooEarly {
        draftTimeValidator.validateDraftTime(Instant.now().plus(11, ChronoUnit.MINUTES));
    }
}