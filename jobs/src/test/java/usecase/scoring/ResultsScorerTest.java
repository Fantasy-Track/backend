package usecase.scoring;

import domain.entity.ScoredResult;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ResultsScorerTest {

    @Test
    public void testScoreResults() {
        ResultsScorer resultsScorer = new ResultsScorer(new ResultsRanker());

        List<ScoredResult> scoredResults = resultsScorer.scoreResultsForAthletesInLeague(ResultsBank.indexedResults, "test");

        assertEquals(scoredResults.size(), 3);

        assertEquals(scoredResults.get(0).rank, 1);
        assertEquals(scoredResults.get(0).fantasyPoints, 67, 0.01);

        assertEquals(scoredResults.get(1).rank, 2);
        assertEquals(scoredResults.get(1).fantasyPoints, 34, 0.01);

        assertEquals(scoredResults.get(2).rank, 3);
        assertEquals(scoredResults.get(2).fantasyPoints, 1, 0.01);
    }

    @Test
    public void testCalcPoints() {
        assertEquals(ResultsScorer.calcPoints(1, 100), 99.01);
    }
}