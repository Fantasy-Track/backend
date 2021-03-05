package usecase.scoring;

import domain.entity.*;
import domain.repository.LeagueRepository;
import mock.LeagueBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ResultsFilterTest {

    private League league = LeagueBank.postDraftLeague;
    private ResultsFilter resultsFilter;

    @BeforeMethod
    public void setUp() {
        LeagueRepository leagueRepository = mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        resultsFilter = new ResultsFilter(leagueRepository);
    }

    @Test
    public void testFilterNotInLeague() {
        List<ScoredResult> filteredResults = resultsFilter.filterLeagueAthletes(List.of(ResultsBank.result4), league.id);
        assertEquals(filteredResults.size(), 0);
    }

    @Test
    public void testFilterInvalidEvent() {
        IndexedResult result = IndexedResult.builder()
                .eventId("1000")
                .athleteId("A1")
                .mark(new Mark("6.90", "1000"))
                .build();
        List<IndexedResult> filteredResults = resultsFilter.filterInvalid(List.of(result));
        assertEquals(filteredResults.size(), 0);
    }

    @Test
    public void testFilterDNS() {
        IndexedResult result = IndexedResult.builder()
                .eventId("9")
                .athleteId("A1")
                .mark(new Mark("DNS", "9"))
                .build();
        List<IndexedResult> filteredResults = resultsFilter.filterInvalid(List.of(result));
        assertEquals(filteredResults.size(), 0);
    }

    @Test
    public void testNoFilter() {
        IndexedResult result = IndexedResult.builder()
                .eventId("9")
                .athleteId("A1")
                .mark(new Mark("12-00", "9"))
                .build();
        List<IndexedResult> filteredResults = resultsFilter.filterInvalid(List.of(result));
        assertEquals(filteredResults.size(), 1);

        RankedResult rankedResult = new RankedResult(1, result);
    }
}