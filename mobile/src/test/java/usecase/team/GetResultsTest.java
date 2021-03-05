package usecase.team;

import mock.MockNameRepo;
import mock.MockResultRepo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.dto.ResultDTO;
import usecase.results.GetResults;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class GetResultsTest {

    GetResults resultsFetcher;

    @BeforeMethod
    public void setUp() {
        resultsFetcher = new GetResults(new MockResultRepo(), new MockNameRepo());
    }

    @Test
    public void testGetTeamResultsAtMeets() {
        List<ResultDTO> results = resultsFetcher.getTeamResultsAtMeet("T1", "M1");

        assertEquals(results.size(), 1);
        assertEquals(results.get(0).meetId, "M1");
        assertEquals(results.get(0).teamId, "T1");
    }


    @Test
    public void testGetNoTeamResultsAtMeets() {
        List<ResultDTO> results = resultsFetcher.getTeamResultsAtMeet("T1", "imaginaryMeet");
        assertEquals(results.size(), 0);
    }

    @Test
    public void testGetAthleteResults() {
        List<ResultDTO> results = resultsFetcher.getAthleteResults("A1", "test");
        assertEquals(results.size(), 3);
        assertEquals(results.get(0).athleteId, "A1");
        assertEquals(results.get(1).athleteId, "A1");
        assertEquals(results.get(2).athleteId, "A1");
    }
}