package domain.repository;

public interface QuotaRepository {

    void triggerMeetsPulledQuota(String leagueId, int minutes);

    void triggerAthletesPulledQuota(String leagueId, int minutes);

    void triggerMeetRescoreQuota(String meetId, int minutes);

    boolean canPullMeets(String leagueId);

    boolean canPullAthletes(String leagueId);

    boolean canRescoreMeet(String meetId);
}
