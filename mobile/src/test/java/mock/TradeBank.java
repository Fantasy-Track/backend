package mock;

import com.fantasytrack.protos.TransactionService;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;

import java.util.List;

public class TradeBank {

    public static TradeProposal proposal = TradeProposal.builder()
            .id("TR1")
            .leagueId("test")
            .status(TradeStatus.PENDING)
            .proposingTeamId("T1")
            .acceptingTeamId("T2")
            .offeringAthletes(List.of("A1"))
            .receivingAthletes(List.of("A2"))
            .build();

}
