package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.TransactionService;
import fantasyapp.grpc.TimeSerializer;
import domain.entity.TradeStatus;
import usecase.dto.TradeProposalDTO;

import java.util.List;

public class TransactionSerializer {

    public static TransactionService.TradesResponse serializeTradeResponse(List<TradeProposalDTO> proposals) {
        TransactionService.TradesResponse.Builder res = TransactionService.TradesResponse.newBuilder();
        proposals.forEach(proposal -> res.addProposals(serializeTradeProposal(proposal)));
        return res.build();
    }

    public static TransactionService.TradeProposal serializeTradeProposal(TradeProposalDTO proposal) {
        return TransactionService.TradeProposal.newBuilder()
                .setId(proposal.id)
                .setStatus(serializeStatus(proposal.status))
                .setProposingTeamId(proposal.proposingTeamId)
                .setProposingTeamName(proposal.proposingTeamName)
                .setAcceptingTeamId(proposal.acceptingTeamId)
                .setAcceptingTeamName(proposal.acceptingTeamName)
                .putAllOfferingAthletes(proposal.offeringAthletes)
                .putAllReceivingAthletes(proposal.receivingAthletes)
                .setDate(TimeSerializer.serializeTime(proposal.date)).build();
    }

    private static TransactionService.TradeProposal.Status serializeStatus(TradeStatus status) {
        switch (status) {
            case REJECTED:
                return TransactionService.TradeProposal.Status.REJECTED;
            case ACCEPTED_WAITING:
                return TransactionService.TradeProposal.Status.ACCEPTED_WAITING;
            case ACCEPTED_ENACTED:
                return TransactionService.TradeProposal.Status.ACCEPTED_ENACTED;
            case CANCELLED:
                return TransactionService.TradeProposal.Status.CANCELLED;
            case PENDING:
            default:
                return TransactionService.TradeProposal.Status.PENDING;
        }
    }

}
