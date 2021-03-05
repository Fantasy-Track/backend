package usecase.athletes;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.entity.Contract;
import domain.repository.AthleteRepository;
import domain.repository.ContractRepository;
import domain.repository.NameRepository;
import usecase.assembler.AthleteAssembler;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAthleteOwnership {

    private AthleteRepository athleteRepository;
    private ContractRepository contractRepository;
    private NameRepository nameRepository;

    @Inject
    public GetAthleteOwnership(AthleteRepository athleteRepository, ContractRepository contractRepository, NameRepository nameRepository) {
        this.athleteRepository = athleteRepository;
        this.contractRepository = contractRepository;
        this.nameRepository = nameRepository;
    }

    public List<AthleteOwnershipDTO> getAthleteOwnership(List<String> athleteIds, String leagueId) {
        List<Athlete> athletes = athleteRepository.getAthletesByIds(athleteIds);

        Map<String, Contract> contracts = contractRepository.getLeagueContracts(leagueId).stream()
                .collect(Collectors.toMap(contract -> contract.athleteId, contract -> contract));

        return athletes.stream().map((athlete) ->
                AthleteAssembler.writeOwnershipDTO(athlete, contracts.get(athlete.id), nameRepository))
                .collect(Collectors.toList());
    }

}
