package domain.repository;

public interface NameRepository {

    String getAthleteName(String athleteId);

    String getMeetName(String meetId);

    String getOwnerName(String ownerId);

    String getLeagueName(String leagueId);

    String getTeamName(String teamId);

    void updateLeagueName(String leagueId, String leagueName);

    void updateTeamName(String teamId, String teamName);

    void updateOwnerName(String ownerId, String ownerName);
}
