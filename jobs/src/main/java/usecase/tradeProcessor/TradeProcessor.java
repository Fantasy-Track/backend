package usecase.tradeProcessor;

import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.exception.TradeNotAccepted;
import domain.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.ContractEditor;
import usecase.ContractRequest;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;
import usecase.trading.TradeInvalidator;
import usecase.trading.TradeValidator;
import util.DistributedLock;

import java.util.List;

public class TradeProcessor {

    private Logger logger = LoggerFactory.getLogger(TradeProcessor.class);

    private TradeValidator validator;
    private ContractEditor contractEditor;
    private TradeRepository tradeRepository;
    private TradeInvalidator tradeInvalidator;
    private DistributedLock lock;

    @Inject
    public TradeProcessor(TradeValidator validator, ContractEditor contractEditor, TradeRepository tradeRepository, TradeInvalidator tradeInvalidator, DistributedLock lock) {
        this.validator = validator;
        this.contractEditor = contractEditor;
        this.tradeRepository = tradeRepository;
        this.tradeInvalidator = tradeInvalidator;
        this.lock = lock;
    }

    public void processTrade(TradeProposal trade) throws ApplicationException {
        lock.lockAndRun(DistributedLock.TRADE_PROCESSING, trade.id, () -> {
            verifyCanAccept(trade);
            lock.lockAndRun(DistributedLock.CONTRACT, trade.leagueId, () -> {
                validator.validate(trade);
                editRoster(trade);
            });
            tradeRepository.setTradeStatus(trade.id, TradeStatus.ACCEPTED_ENACTED);
        });
        tradeInvalidator.cancelInvalidTrades(trade.leagueId);

        //send notifications to both teams
        NotificationFactory.handler.sendTradeProcessed(trade.proposingTeamId);
        NotificationFactory.handler.sendTradeProcessed(trade.acceptingTeamId);
    }

    private void verifyCanAccept(TradeProposal trade) throws TradeNotAccepted {
        if (trade.status != TradeStatus.ACCEPTED_WAITING) {
            logger.warn("Tried to process a trade but it was not accepted and waiting. Actual status: " + trade.status);
            throw new TradeNotAccepted();
        }
    }

    private void editRoster(TradeProposal trade) throws ApplicationException {
        moveAthletes(trade.offeringAthletes, trade.proposingTeamId, trade.acceptingTeamId, trade.leagueId);
        moveAthletes(trade.receivingAthletes, trade.acceptingTeamId, trade.proposingTeamId, trade.leagueId);
    }

    private void moveAthletes(List<String> athletes, String fromTeam, String toTeam, String leagueId) throws ApplicationException {
        for (String athleteId : athletes) {
            contractEditor.deleteContract(athleteId, fromTeam);
            contractEditor.signContract(ContractRequest.builder()
                    .athleteId(athleteId)
                    .teamId(toTeam)
                    .leagueId(leagueId)
                    .build());
        }
    }

}
