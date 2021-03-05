package usecase.scoring;

import com.google.inject.Inject;
import domain.entity.IndexedResult;
import domain.entity.RankedResult;
import domain.entity.ScoredResult;

import java.util.List;
import java.util.stream.Collectors;

public class ResultsScorer {

    private ResultsRanker ranker;

    @Inject
    public ResultsScorer(ResultsRanker ranker) {
        this.ranker = ranker;
    }

    public List<ScoredResult> scoreResultsForAthletesInLeague(List<IndexedResult> indexedResults, String leagueId) {
        List<RankedResult> rankedResults = ranker.rankResults(indexedResults);
        return rankedResults.stream().map(rankedResult -> {
            double points = calcPoints(rankedResult.rank, rankedResults.size());
            return new ScoredResult(points, rankedResult);
        }).collect(Collectors.toList());
    }

    public static double calcPoints(int rank, int totalParticipants) {
        return 1 + 99 * ((double) (totalParticipants - rank) / totalParticipants);
    }

}
