package domain.entity;

public class ScoredResult {

    public final double fantasyPoints;
    public final int rank;
    public final IndexedResult result;

    public ScoredResult(double fantasyPoints, RankedResult rankedResult) {
        this.fantasyPoints = fantasyPoints;
        this.result = rankedResult.indexedResult;
        this.rank = rankedResult.rank;
    }

    @Override
    public String toString() {
        return String.format("Rank: %d, Points: %f, " +
                "Mark: %s", rank, fantasyPoints, result);
    }
}
