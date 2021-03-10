package domain.repository;

import domain.entity.ScoredResult;

import java.util.List;

public interface ResultRepository {

    void addResult(ScoredResult result, String teamId);

    void addResult(ScoredResult result);

    double aggregatePointsAtMeets(String teamId, List<String> meetIds);

    void removeMeetResults(String meetId);

}
