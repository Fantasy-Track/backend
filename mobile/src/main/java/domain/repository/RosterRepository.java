package domain.repository;

public interface RosterRepository {

    boolean isAthleteOnRoster(String athleteId, String teamId);

    int countAthletesInEventCategory(String eventCategoryId, String teamId);

    void setAthleteEvent(String athleteId, String eventId, String teamId);

}
