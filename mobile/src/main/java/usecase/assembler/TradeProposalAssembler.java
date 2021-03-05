package usecase.assembler;

import domain.entity.TradeProposal;
import domain.repository.NameRepository;
import usecase.dto.TradeProposalDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TradeProposalAssembler {

    public static TradeProposalDTO writeDTO(TradeProposal proposal, NameRepository nameRepository) {
        TradeProposalDTO.TradeProposalDTOBuilder builder = TradeProposalDTO.builder();
        return builder
                .id(proposal.id)
                .status(proposal.status)
                .acceptingTeamId(proposal.acceptingTeamId)
                .acceptingTeamName(nameRepository.getTeamName(proposal.acceptingTeamId))
                .proposingTeamId(proposal.proposingTeamId)
                .proposingTeamName(nameRepository.getTeamName(proposal.proposingTeamId))
                .offeringAthletes(mapAthleteIdsToName(proposal.offeringAthletes, nameRepository))
                .receivingAthletes(mapAthleteIdsToName(proposal.receivingAthletes, nameRepository))
                .date(proposal.date)
                .build();
    }

    private static Map<String, String> mapAthleteIdsToName(List<String> athleteIds, NameRepository nameRepository) {
        return athleteIds.stream().collect(
                Collectors.toMap(id -> id, nameRepository::getAthleteName, (s, s2) -> s, HashMap::new));
    }

}
