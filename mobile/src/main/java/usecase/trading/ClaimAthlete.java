package usecase.trading;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.ApplicationException;
import domain.exception.CannotDoActionNow;
import domain.exception.TeamFull;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.ContractEditor;
import usecase.ContractRequest;
import util.DistributedLock;

public class ClaimAthlete {

    private Logger logger = LoggerFactory.getLogger(ClaimAthlete.class);

    private DistributedLock lock;
    private ContractEditor signer;
    private ContractRepository contractRepository;
    private LeagueRepository leagueRepository;

    @Inject
    public ClaimAthlete(ContractEditor signer, ContractRepository contractRepository, LeagueRepository leagueRepository, DistributedLock lock) {
        this.signer = signer;
        this.contractRepository = contractRepository;
        this.leagueRepository = leagueRepository;
        this.lock = lock;
    }

    public void claimAthlete(ClaimAthleteRequest request) throws ApplicationException {
        logger.info("Attempting to claim athlete: " + request.athleteId);
        lock.lockAndRun(DistributedLock.CONTRACT, request.leagueId, () -> {
            validateClaim(request);
            signer.signContract(ContractRequest.builder()
                    .leagueId(request.leagueId)
                    .athleteId(request.athleteId)
                    .teamId(request.teamId)
                    .build());
        });
        logger.info("Successfully claimed athlete: " + request.athleteId);
    }

    private void validateClaim(ClaimAthleteRequest request) throws CannotDoActionNow, TeamFull {
        int teamSize = contractRepository.getTeamContracts(request.teamId).size();
        League league = leagueRepository.getLeagueById(request.leagueId);
        if (league.status != LeagueStatus.POST_DRAFT) {
            throw new CannotDoActionNow();
        } else if (teamSize >= league.leagueSettings.teamSize) {
            throw new TeamFull();
        }
    }

}
