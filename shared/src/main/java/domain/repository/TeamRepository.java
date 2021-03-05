package domain.repository;

import domain.entity.Team;

import java.util.List;

public interface TeamRepository {

    boolean isNameTaken(String name, String leagueId);

    boolean doesOwnerHaveTeamInLeague(String ownerId, String leagueId);

    Team getTeamById(String teamId);

    void addTeam(Team team);

    void deleteTeam(String teamId);

    void updateFantasyPoints(String teamId, double points);

    List<Team> getOwnedTeams(String ownerId);

    List<Team> getTeamsInLeague(String leagueId);

    int countTeamsInLeague(String leagueId);

}
