package usecase;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.entity.Contract;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.repository.ContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ContractEditor {

    private Logger logger = LoggerFactory.getLogger(ContractEditor.class);

    private final ContractValidator validator;
    private final ContractRepository contractRepository;

    @Inject
    public ContractEditor(ContractValidator contractValidator, ContractRepository contractRepository) {
        this.validator = contractValidator;
        this.contractRepository = contractRepository;
    }

    // Note: this method does not enforce any rules about the size of the team
    // It does however, require that the athlete is a free agent
    public void signContract(ContractRequest request) throws ApplicationException {
        Contract contract = Contract.builder()
                .athleteId(request.athleteId)
                .teamId(request.teamId)
                .eventId("-5")
                .categoryId(Events.BENCH_ID)
                .leagueId(request.leagueId)
                .dateSigned(Instant.now()).build();

        validator.validateContract(contract);
        contractRepository.signContract(contract);
    }

    public void deleteContract(String athleteId, String teamId) throws AthleteNotOnTeam {
        boolean onTeam = contractRepository.isAthleteOnTeam(athleteId, teamId);
        if (!onTeam) {
            logger.info(String.format("Cannot drop athlete %s - not on team %s", athleteId, teamId));
            throw new AthleteNotOnTeam();
        }
        contractRepository.deleteContract(athleteId, teamId);
    }

}
