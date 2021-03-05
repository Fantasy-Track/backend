package usecase.trading;

import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import java.time.Instant;
import java.util.UUID;

public class TradeProposer {

    private Logger logger = LoggerFactory.getLogger(TradeProposer.class);

    private TradeValidator validator;
    private TradeRepository tradeRepository;

    @Inject
    public TradeProposer(TradeValidator validator, TradeRepository tradeRepository) {
        this.validator = validator;
        this.tradeRepository = tradeRepository;
    }

    public void proposeTrade(TradeProposalRequest request) throws ApplicationException {
        TradeProposal proposal = buildProposal(request);
        validator.validate(proposal);
        tradeRepository.addTradeProposal(proposal);
        logger.info("Trade proposed successfully");
        NotificationFactory.handler.sendTradeProposed(request.proposingTeamId, request.acceptingTeamId);
    }

    private TradeProposal buildProposal(TradeProposalRequest request) {
        return TradeProposal.builder()
                .id(UUID.randomUUID().toString())
                .status(TradeStatus.PENDING)
                .proposingTeamId(request.proposingTeamId)
                .acceptingTeamId(request.acceptingTeamId)
                .leagueId(request.leagueId)
                .offeringAthletes(request.offeringAthletes)
                .receivingAthletes(request.receivingAthletes)
                .date(Instant.now())
                .build();
    }
}
