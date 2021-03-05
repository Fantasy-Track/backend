package usecase.trading;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.exception.CannotDoActionNow;
import domain.repository.LeagueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.ContractEditor;
import util.DistributedLock;

public class DropAthlete {

    private final Logger logger = LoggerFactory.getLogger(DropAthlete.class);
    private ContractEditor contractEditor;
    private LeagueRepository leagueRepository;
    private TradeInvalidator tradeInvalidator;
    private DistributedLock lock;

    @Inject
    public DropAthlete(ContractEditor contractEditor, LeagueRepository leagueRepository, TradeInvalidator tradeInvalidator, DistributedLock lock) {
        this.contractEditor = contractEditor;
        this.leagueRepository = leagueRepository;
        this.tradeInvalidator = tradeInvalidator;
        this.lock = lock;
    }

    public void dropAthletes(DropAthletesRequest dropRequest) throws ApplicationException {
        logger.info("Attempting to drop athletes: " + dropRequest.athleteIds.toString());
        lock.lockAndRun(DistributedLock.CONTRACT, dropRequest.leagueId, () -> {
            drop(dropRequest);
        });
        tradeInvalidator.cancelInvalidTrades(dropRequest.leagueId);
        logger.info("Athletes dropped successfully: " + dropRequest.athleteIds.toString());
    }

    private void drop(DropAthletesRequest request) throws AthleteNotOnTeam, CannotDoActionNow {
        validateLeagueStatus(request);
        for (String athleteId : request.athleteIds) {
            contractEditor.deleteContract(athleteId, request.teamId);
        }
    }

    private void validateLeagueStatus(DropAthletesRequest request) throws CannotDoActionNow {
        League league = leagueRepository.getLeagueById(request.leagueId);
        if (league.status != LeagueStatus.POST_DRAFT) throw new CannotDoActionNow();
    }

}
