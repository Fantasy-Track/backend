package domain.repository;

import domain.entity.League;
import domain.entity.LeagueStatus;

public interface LeagueRepository {

    League getLeagueById(String leagueId);

    void setStatus(String leagueId, LeagueStatus status);

    boolean isNameTaken(String name);

    void addLeague(League league);

}
