package usecase.dto;

import domain.entity.TradeStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public class TradeProposalDTO {

    public final String id;

    public final String proposingTeamId, proposingTeamName;
    public final String acceptingTeamId, acceptingTeamName;

    public final Instant date;

    public final Map<String, String> receivingAthletes; //id, name
    public final Map<String, String> offeringAthletes;

    public final TradeStatus status;

}
