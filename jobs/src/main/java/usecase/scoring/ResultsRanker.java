package usecase.scoring;

import domain.entity.IndexedResult;
import domain.entity.RankedResult;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ResultsRanker {

    public List<RankedResult> rankResults(List<IndexedResult> results) {
        results.sort(Comparator.comparingInt(result -> -result.mark.getIntValue()));
        List<RankedResult> rankedResults = new LinkedList<>();

        int lastScore = Integer.MIN_VALUE;
        int lastRank = 1;
        for (int i = 0; i < results.size(); i++) {
            IndexedResult result = results.get(i);
            if (lastScore != results.get(i).mark.getIntValue()) {
                lastRank = i + 1;
            }
            rankedResults.add(new RankedResult(lastRank, result));
            lastScore = result.mark.getIntValue();
        }
        return rankedResults;
    }

}
