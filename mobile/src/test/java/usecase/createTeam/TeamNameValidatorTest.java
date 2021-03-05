package usecase.createTeam;

import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.TeamRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamNameValidatorTest {

    private String leagueId = "test";
    private TeamNameValidator verifier;

    @BeforeMethod
    public void setUp() {
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.isNameTaken("taken", leagueId)).then(invocation -> true);
        verifier = new TeamNameValidator(teamRepository);
    }

    @Test
    public void testNameTaken() throws NameTooShort, NameTooLong {
        try {
            verifier.validateName("taken", leagueId);
            Assert.fail();
        } catch (NameTaken ignored) { }
    }

    @Test
    public void testValid() {
        try {
            verifier.validateName("Bob", leagueId);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testTooLong() throws NameTooShort, NameTaken {
        try {
            verifier.validateName("abcdefghijklmnolp", leagueId);
            Assert.fail();
        } catch (NameTooLong ignored) {}
    }

    @Test
    public void testTooShort() throws NameTooLong, NameTaken {
        try {
            verifier.validateName("gg", leagueId);
            Assert.fail();
        } catch (NameTooShort ignored) {}
    }

}