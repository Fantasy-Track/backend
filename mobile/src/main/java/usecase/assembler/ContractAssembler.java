package usecase.assembler;

import domain.entity.Contract;
import usecase.dto.ContractDTO;

public class ContractAssembler {

    public static ContractDTO writeDTO(Contract contract) {
        return ContractDTO.builder()
                .athleteId(contract.athleteId)
                .teamId(contract.teamId)
                .dateSigned(contract.dateSigned)
                .leagueId(contract.leagueId)
                .eventId(contract.eventId)
                .build();
    }

}
