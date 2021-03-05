package usecase.team;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.entity.Contract;
import domain.entity.Meet;
import domain.repository.AthleteRepository;
import domain.repository.ContractRepository;
import domain.repository.MeetRepository;
import domain.repository.NameRepository;
import usecase.assembler.AthleteAssembler;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetTeamRoster {

    private ContractRepository contractRepository;
    private AthleteRepository athleteRepository;
    private NameRepository nameRepository;
    private MeetRepository meetRepository;

    @Inject
    public GetTeamRoster(ContractRepository contractRepository, AthleteRepository athleteRepository, NameRepository nameRepository, MeetRepository meetRepository) {
        this.contractRepository = contractRepository;
        this.athleteRepository = athleteRepository;
        this.nameRepository = nameRepository;
        this.meetRepository = meetRepository;
    }

    public List<AthleteOwnershipDTO> getRoster(String teamId, String meetId) {
        if (meetId == null) return getCurrentRoster(teamId);

        Meet meet = meetRepository.getMeetById(meetId);
        List<Contract> teamContracts = meet.savedContracts.stream()
                .filter(contract -> contract.teamId.equals(teamId))
                .collect(Collectors.toList());
        return getRosterFromContracts(teamContracts);
    }

    private List<AthleteOwnershipDTO> getCurrentRoster(String teamId) {
        List<Contract> teamContracts = contractRepository.getTeamContracts(teamId);
        return getRosterFromContracts(teamContracts);
    }

    private List<AthleteOwnershipDTO> getRosterFromContracts(List<Contract> contracts) {
        Map<String, Contract> contractMap = contracts.stream().collect(Collectors.toMap(contract -> contract.athleteId, o -> o));
        List<Athlete> athletes = athleteRepository.getAthletesByIds(contractMap.values().stream().map(contract -> contract.athleteId).collect(Collectors.toList()));
        return athletes.stream().map(athlete -> AthleteAssembler.writeOwnershipDTO(athlete, contractMap.get(athlete.id), nameRepository)).collect(Collectors.toList());
    }

}
