package usecase.trading;

import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.exception.TradeNotExists;
import domain.repository.NameRepository;
import domain.repository.TradeRepository;
import usecase.assembler.TradeProposalAssembler;
import usecase.dto.TradeProposalDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TradeProposalFetcher {

    private TradeRepository tradeRepository;
    private NameRepository nameRepository;

    @Inject
    public TradeProposalFetcher(TradeRepository tradeRepository, NameRepository nameRepository) {
        this.tradeRepository = tradeRepository;
        this.nameRepository = nameRepository;
    }

    public List<TradeProposalDTO> getTradesWithTeam(String teamId) {
        return tradeRepository.getTradesWithTeam(teamId).stream().map(proposal -> TradeProposalAssembler.writeDTO(proposal, nameRepository))
                .collect(Collectors.toList());
    }

    public TradeProposalDTO getTradeById(String id) throws TradeNotExists {
        TradeProposal trade = tradeRepository.getTradeById(id);
        if (trade == null) throw new TradeNotExists();
        return TradeProposalAssembler.writeDTO(trade, nameRepository);
    }


    public List<TradeProposalDTO> getAcceptedTradesInLeague(String leagueId) {
        return tradeRepository.getAcceptedTradesInLeague(leagueId).stream().map(proposal -> TradeProposalAssembler.writeDTO(proposal, nameRepository))
                .collect(Collectors.toList());
    }
}
