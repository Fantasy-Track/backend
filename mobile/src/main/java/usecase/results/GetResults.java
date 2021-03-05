package usecase.results;

import com.google.inject.Inject;
import domain.entity.Result;
import domain.repository.NameRepository;
import domain.repository.ResultRepository;
import usecase.assembler.ResultAssembler;
import usecase.dto.ResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GetResults {

    private ResultRepository resultRepository;
    private NameRepository nameRepository;

    @Inject
    public GetResults(ResultRepository resultRepository, NameRepository nameRepository) {
        this.resultRepository = resultRepository;
        this.nameRepository = nameRepository;
    }

    public List<ResultDTO> getMeetResults(String meetId) {
        List<Result> results = resultRepository.getResultsAtMeet(meetId);
        return resultsToDTOList(results);
    }

    public List<ResultDTO> getTeamResultsAtMeet(String teamId, String meetId) {
        List<Result> results = resultRepository.getTeamResultsAtMeet(teamId, meetId);
        return resultsToDTOList(results);
    }

    public List<ResultDTO> getAthleteResults(String athleteId, String leagueId) {
        List<Result> results = resultRepository.getAthleteResultsInLeague(athleteId, leagueId);
        return resultsToDTOList(results);
    }

    private List<ResultDTO> resultsToDTOList(List<Result> results) {
        return results.stream().map(
                (Result result) -> ResultAssembler.writeDTO(result,
                        nameRepository))
                .collect(Collectors.toList());
    }

}
