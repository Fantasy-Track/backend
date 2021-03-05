package domain.repository;

public interface QuotaRepository {

    void triggerMeetsPulledQuota(String leagueId, int minutes);

    void triggerAthletesPulledQuota(String leagueId, int minutes);

    boolean canPullMeets(String leagueId);

    boolean canPullAthletes(String leagueId);
}
