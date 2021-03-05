package usecase.results;

import domain.entity.Team;
import domain.exception.TeamNotExists;
import domain.repository.TeamRepository;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class GetPointsTest {

    GetPoints pointsFetcher;

    @BeforeMethod
    public void setUp() {
        TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
        when(teamRepository.getTeamById("T1")).then(invocation -> Team.builder().fantasyPoints(98).build());
        pointsFetcher = new GetPoints(teamRepository);
    }

    @Test
    public void testGetPoints() throws TeamNotExists {
        double points = pointsFetcher.getPoints("T1");
        assertEquals(points, 98.0, 0.0001);
    }

    @Test
    public void testNoPoints() {
        try {
            pointsFetcher.getPoints("imaginaryTeam");
            Assert.fail();
        } catch (TeamNotExists ignored) { }
    }

}