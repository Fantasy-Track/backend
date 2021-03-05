package usecase.league;

import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.LeagueRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeagueNameValidatorTest {

    private LeagueNameValidator verifier;

    @BeforeMethod
    public void setUp() {
        verifier = new LeagueNameValidator();
    }

    @Test
    public void testValid() {
        try {
            verifier.validateName("Bob");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testTooLong() throws NameTooShort, NameTaken {
        try {
            verifier.validateName("abcdefghijklmnolpqr");
            Assert.fail();
        } catch (NameTooLong ignored) {}
    }

    @Test
    public void testTooShort() throws NameTooLong, NameTaken {
        try {
            verifier.validateName("gg");
            Assert.fail();
        } catch (NameTooShort ignored) {}
    }

}