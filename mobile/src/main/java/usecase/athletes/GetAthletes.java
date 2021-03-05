package usecase.athletes;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.entity.League;
import domain.exception.AthleteNotExists;
import domain.exception.LeagueNotExists;
import domain.repository.AthleteRepository;
import domain.repository.LeagueRepository;
import usecase.assembler.AthleteAssembler;
import usecase.dto.AthleteDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAthletes {

    private LeagueRepository leagueRepository;
    private AthleteRepository athleteRepository;

    @Inject
    public GetAthletes(LeagueRepository leagueRepository, AthleteRepository athleteRepository) {
        this.leagueRepository = leagueRepository;
        this.athleteRepository = athleteRepository;
    }

    public List<String> getAthleteIdsInLeague(String leagueId) throws LeagueNotExists {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) throw new LeagueNotExists();
        return league.athletes;
    }

    public List<AthleteDTO> getAthletesInLeague(String leagueId) throws LeagueNotExists {
        List<String> athleteIds = getAthleteIdsInLeague(leagueId);
        List<Athlete> athletes = athleteRepository.getAthletesByIds(athleteIds);
        return athletes.stream().map(AthleteAssembler::writeAthleteDTO)
                .collect(Collectors.toList());
    }

    public AthleteDTO getOneAthlete(String athleteId) throws AthleteNotExists {
        Athlete athlete = athleteRepository.getAthleteById(athleteId);
        if (athlete == null) throw new AthleteNotExists();
        return AthleteAssembler.writeAthleteDTO(athlete);
    }

}
