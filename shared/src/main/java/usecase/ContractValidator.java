package usecase;

import com.google.inject.Inject;
import domain.entity.Contract;
import domain.exception.AgentNotFree;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotExists;
import domain.repository.AthleteRepository;
import domain.repository.ContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractValidator {

    private final Logger logger = LoggerFactory.getLogger(ContractValidator.class);

    private final ContractRepository contractRepository;
    private final AthleteRepository athleteRepository;

    @Inject
    public ContractValidator(ContractRepository contractRepository, AthleteRepository athleteRepository) {
        this.contractRepository = contractRepository;
        this.athleteRepository = athleteRepository;
    }

    public void validateContract(Contract contract) throws ApplicationException {
        boolean athleteInLeague = athleteRepository.isAthleteInLeague(contract.athleteId, contract.leagueId);
        if (!athleteInLeague) {
            logger.info("Athlete not in league: " + contract);
            throw new AthleteNotExists();
        }

        if(!contractRepository.isAthleteFreeAgent(contract.athleteId, contract.leagueId)) {
            logger.info("Contract Invalid (Not Free Agent): " + contract);
            throw new AgentNotFree();
        }
    }


}
