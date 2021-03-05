package domain.entity;

public class RankedResult {

    public final IndexedResult indexedResult;
    public final int rank;

    public RankedResult(int rank, IndexedResult indexedResult) {
        this.indexedResult = indexedResult;
        this.rank = rank;
    }

}
