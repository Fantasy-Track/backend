package usecase.scoring;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.entity.IndexedResult;
import domain.entity.League;
import domain.entity.ScoredResult;
import domain.repository.LeagueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultsFilter {

    private LeagueRepository leagueRepository;

    @Inject
    public ResultsFilter(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public List<IndexedResult> filterInvalid(List<IndexedResult> results) {
        return results.stream().filter(result ->
                result.mark.isMarkValid() && Events.isValidEvent(result.eventId))
                .collect(Collectors.toList());
    }

    public List<ScoredResult> filterLeagueAthletes(List<ScoredResult> results, String leagueId) {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) return new ArrayList<>();

        return results.stream().filter(result -> league.athletes.contains(result.result.athleteId))
                .collect(Collectors.toList());
    }
}
