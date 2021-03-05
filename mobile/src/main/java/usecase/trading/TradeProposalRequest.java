package usecase.trading;

import lombok.Builder;

import java.util.List;

@Builder
public class TradeProposalRequest {

    public final String proposingTeamId;
    public final String acceptingTeamId;
    public final String leagueId;

    public final List<String> receivingAthletes;
    public final List<String> offeringAthletes;
}
