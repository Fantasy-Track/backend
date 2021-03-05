package domain.repository;

import domain.entity.Athlete;

import java.util.List;

public interface AthleteUpdateRepository extends AthleteRepository {

    void setCategoryProjection(String athleteId, String categoryId, double projection);

    void addAthlete(Athlete athlete);

    void setPrimaryEvents(String id, List<String> eventIds);

}
