package domain.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public class TradeProposal {

    public final String id;

    public final String proposingTeamId;
    public final String acceptingTeamId;
    public final String leagueId;

    public final Instant date;

    public final List<String> receivingAthletes;
    public final List<String> offeringAthletes;

    public final TradeStatus status;

}
