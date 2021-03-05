package domain.repository;

import domain.entity.Result;

import java.util.List;

public interface ResultRepository {

    List<Result> getTeamResultsAtMeet(String teamId, String meetId);

    List<Result> getAthleteResultsInLeague(String athleteId, String leagueId);

    List<Result> getResultsAtMeet(String meetId);
}
