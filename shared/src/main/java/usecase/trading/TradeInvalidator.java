package usecase.trading;

import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.repository.TradeRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DistributedLock;

import java.util.List;

public class TradeInvalidator {

    private Logger logger = LoggerFactory.getLogger(TradeInvalidator.class);

    private TradeRepository tradeRepository;
    private TradeValidator validator;
    private DistributedLock lock;

    @Inject
    public TradeInvalidator(TradeRepository tradeRepository, TradeValidator validator, DistributedLock lock) {
        this.tradeRepository = tradeRepository;
        this.validator = validator;
        this.lock = lock;
    }

    @SneakyThrows
    public void cancelInvalidTrades(String leagueId) {
        logger.info("Check for invalid trades to cancel");
        List<TradeProposal> trades = tradeRepository.getIncompleteTradesInLeague(leagueId);
        for (TradeProposal proposal : trades) {
            lock.lockAndRun(DistributedLock.TRADE_PROCESSING, proposal.id, () -> cancelTradeIfInvalid(proposal));
        }
    }

    private void cancelTradeIfInvalid(TradeProposal proposal) {
        try {
            validator.validate(proposal);
        } catch (ApplicationException e) {
            logger.info("Trade invalid, cancelling it. ID: " + proposal.id);
            tradeRepository.setTradeStatus(proposal.id, TradeStatus.CANCELLED);
        }
    }

}
