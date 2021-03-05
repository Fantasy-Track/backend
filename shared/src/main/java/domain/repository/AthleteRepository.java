package domain.repository;

import domain.entity.Athlete;

import java.util.List;
import java.util.Set;

public interface AthleteRepository {

    Athlete getAthleteById(String id);

    List<Athlete> getAthletesByIds(List<String> ids);

    boolean isAthleteInLeague(String athleteId, String leagueId);

}
