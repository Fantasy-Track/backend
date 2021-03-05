package usecase.draftStateSaver;

import domain.entity.Contract;
import domain.repository.NameRepository;
import usecase.dto.ContractDTO;

import javax.inject.Inject;

public class ContractAssembler {

    private NameRepository nameRepository;

    @Inject
    public ContractAssembler(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public ContractDTO writeDTO(Contract contract) {
        return ContractDTO.builder()
                .athleteId(contract.athleteId)
                .athleteName(nameRepository.getAthleteName(contract.athleteId))
                .teamId(contract.teamId)
                .teamName(nameRepository.getTeamName(contract.teamId))
                .dateSigned(contract.dateSigned)
                .build();
    }

}
