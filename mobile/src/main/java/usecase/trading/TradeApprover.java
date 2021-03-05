package usecase.trading;

import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.exception.TradeNotExists;
import domain.exception.TradeNotPending;
import domain.exception.UnauthorizedException;
import domain.repository.TradeRepository;
import usecase.notification.NotificationFactory;

public class TradeApprover {

    private TradeValidator validator;
    private TradeRepository tradeRepository;

    @Inject
    public TradeApprover(TradeValidator validator, TradeRepository tradeRepository) {
        this.validator = validator;
        this.tradeRepository = tradeRepository;
    }

    public void rejectTrade(String tradeId, String teamId) throws ApplicationException {
        TradeProposal proposal = getAndCheckTrade(tradeId);
        if (!proposal.acceptingTeamId.equals(teamId)) throw new UnauthorizedException();
        tradeRepository.setTradeStatus(proposal.id, TradeStatus.REJECTED);
        NotificationFactory.handler.sendTradeRejected(proposal.proposingTeamId, proposal.acceptingTeamId);
    }

    public void acceptTrade(String tradeId, String teamId) throws ApplicationException {
        TradeProposal proposal = getAndCheckTrade(tradeId);
        if (!proposal.acceptingTeamId.equals(teamId)) throw new UnauthorizedException();
        validator.validate(proposal);
        tradeRepository.setTradeStatus(proposal.id, TradeStatus.ACCEPTED_WAITING);
        NotificationFactory.handler.sendTradeAccepted(proposal.proposingTeamId, proposal.acceptingTeamId);
    }

    public void cancelTrade(String tradeId, String teamId) throws ApplicationException {
        TradeProposal proposal = getAndCheckTrade(tradeId);
        if (!proposal.proposingTeamId.equals(teamId)) throw new UnauthorizedException();
        tradeRepository.setTradeStatus(proposal.id, TradeStatus.CANCELLED);
    }


    private TradeProposal getAndCheckTrade(String tradeId) throws ApplicationException {
        TradeProposal proposal = tradeRepository.getTradeById(tradeId);
        if (proposal == null) throw new TradeNotExists();
        else if (proposal.status != TradeStatus.PENDING) throw new TradeNotPending();
        return proposal;
    }

}
