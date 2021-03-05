package mock;

import domain.entity.Contract;
import org.testng.collections.Lists;

import java.time.Instant;
import java.util.List;

public class ContractBank {

    public static Contract contract1 = Contract.builder()
            .athleteId("A1")
            .eventId("18")
            .dateSigned(Instant.now())
            .leagueId("test")
            .teamId("T1").build();

    public static Contract contract2 = Contract.builder()
            .athleteId("A2")
            .eventId("-5")
            .dateSigned(Instant.now())
            .leagueId("test")
            .teamId("T1").build();

    public static Contract contract3 = Contract.builder()
            .athleteId("A3")
            .eventId("18")
            .dateSigned(Instant.now())
            .leagueId("test")
            .teamId("T2").build();

    public static List<Contract> team1Contracts = List.of(contract1, contract2);

    public static List<Contract> leagueContracts = Lists.newArrayList(contract1, contract2, contract3);

}
