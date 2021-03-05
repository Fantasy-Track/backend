package domain.repository;

import domain.entity.TradeProposal;
import domain.entity.TradeStatus;

import java.util.Arrays;
import java.util.List;

public interface TradeRepository {

    void addTradeProposal(TradeProposal proposal);

    List<TradeProposal> getTradesWithTeam(String teamId);

    TradeProposal getTradeById(String id);

    void setTradeStatus(String id, TradeStatus status);

    List<TradeProposal> getTradesWithStatus(TradeStatus status);

    List<TradeProposal> getIncompleteTradesInLeague(String leagueId);

    List<TradeProposal> getAcceptedTradesInLeague(String leagueId);
}
