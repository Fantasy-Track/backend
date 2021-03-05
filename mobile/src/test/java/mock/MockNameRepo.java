package mock;

import domain.repository.NameRepository;

public class MockNameRepo implements NameRepository {

    @Override
    public String getAthleteName(String athleteId) {
        return athleteId + "_name";
    }

    @Override
    public String getMeetName(String meetId) {
        return meetId + "_name";
    }

    @Override
    public String getOwnerName(String ownerId) {
        return ownerId + "_name";
    }

    @Override
    public String getLeagueName(String leagueId) {
        return leagueId + "_name";
    }

    @Override
    public String getTeamName(String teamId) {
        return teamId + "_name";
    }

    @Override
    public void updateLeagueName(String leagueId, String leagueName) {

    }

    @Override
    public void updateTeamName(String teamId, String teamName) {

    }

    @Override
    public void updateOwnerName(String ownerId, String ownerName) {

    }
}
