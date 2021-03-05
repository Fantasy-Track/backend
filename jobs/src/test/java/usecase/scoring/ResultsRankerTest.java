package usecase.scoring;

import domain.entity.RankedResult;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ResultsRankerTest {

    @Test
    public void testFieldRanking() {
        ResultsRanker ranker = new ResultsRanker();
        List<RankedResult> rankedResults = ranker.rankResults(ResultsBank.fieldResults);

        assertEquals(rankedResults.size(), 4);

        assertEquals(rankedResults.get(0).indexedResult.athleteId, "A3");
        assertEquals(rankedResults.get(0).rank, 1);

        assertEquals(rankedResults.get(1).indexedResult.athleteId, "A4");
        assertEquals(rankedResults.get(1).rank, 1);

        assertEquals(rankedResults.get(2).indexedResult.athleteId, "A1");
        assertEquals(rankedResults.get(2).rank, 3);

        assertEquals(rankedResults.get(3).indexedResult.athleteId, "A2");
        assertEquals(rankedResults.get(3).rank, 4);
    }

    @Test
    public void testRunningRankings() {
        ResultsRanker ranker = new ResultsRanker();
        List<RankedResult> rankedResults = ranker.rankResults(ResultsBank.sprintEvents);

        assertEquals(rankedResults.size(), 4);

        assertEquals(rankedResults.get(0).indexedResult.athleteId, "A2");
        assertEquals(rankedResults.get(0).rank, 1);

        assertEquals(rankedResults.get(1).indexedResult.athleteId, "A3");
        assertEquals(rankedResults.get(1).rank, 2);

        assertEquals(rankedResults.get(2).indexedResult.athleteId, "A4");
        assertEquals(rankedResults.get(2).rank, 2);

        assertEquals(rankedResults.get(3).indexedResult.athleteId, "A1");
        assertEquals(rankedResults.get(3).rank, 4);
    }
}