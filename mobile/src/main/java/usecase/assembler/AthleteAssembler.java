package usecase.assembler;

import domain.entity.Athlete;
import domain.entity.Contract;
import domain.repository.NameRepository;
import usecase.dto.AthleteDTO;
import usecase.dto.AthleteOwnershipDTO;

public class AthleteAssembler {

    public static AthleteDTO writeAthleteDTO(Athlete athlete) {
        return AthleteDTO.builder()
                .id(athlete.id)
                .name(athlete.name)
                .gender(athlete.gender)
                .primaryEvents(athlete.primaryEvents)
                .projections(athlete.projections)
                .build();
    }

    public static AthleteOwnershipDTO writeOwnershipDTO(Athlete athlete, Contract contract, NameRepository nameRepository) {
        return AthleteOwnershipDTO.builder()
                .teamId(contract == null ? null : contract.teamId)
                .teamName(contract == null ? null : nameRepository.getTeamName(contract.teamId))
                .positionId(contract == null ? null : contract.eventId)
                .owned(contract != null)
                .id(athlete.id)
                .name(athlete.name)
                .gender(athlete.gender)
                .primaryEvents(athlete.primaryEvents)
                .projections(athlete.projections)
                .build();
    }

}
