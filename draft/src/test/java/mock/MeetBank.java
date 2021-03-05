package mock;

import domain.entity.Meet;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MeetBank {

    public static List<Meet> meets = List.of(
            Meet.builder().id("M1").leagueId("test").date(Instant.now()).name("Meet 1").build()
    );

    public static Meet testMeet1 = Meet.builder()
            .id("MT1")
            .name("Test Meet")
            .athleticId("AID1")
            .leagueId("test")
            .hasResults(true)
            .savedContracts(ContractBank.leagueContracts)
            .date(Instant.now().plus(2, ChronoUnit.DAYS))
            .locked(true)
            .enabled(false)
            .build();

    public static Meet testMeet2 = Meet.builder()
            .id("MT2")
            .name("Test Meet 2")
            .athleticId("AID2")
            .leagueId("test")
            .hasResults(false)
            .locked(false)
            .enabled(true)
            .savedContracts(new ArrayList<>())
            .date(Instant.now().plus(2, ChronoUnit.DAYS))
            .build();

    public static Meet testMeet3 = Meet.builder()
            .id("MT3")
            .name("Test Meet 3")
            .leagueId("other")
            .hasResults(false)
            .locked(false)
            .enabled(false)
            .savedContracts(new ArrayList<>())
            .date(Instant.now())
            .build();

    public static Meet testMeet4 = Meet.builder()
            .id("MT4")
            .name("Test Meet 4")
            .athleticId("AID4")
            .leagueId("test")
            .hasResults(false)
            .locked(false)
            .enabled(true)
            .savedContracts(new ArrayList<>())
            .date(Instant.now())
            .build();

    public static Meet testMeet5 = Meet.builder()
            .id("MT5")
            .name("Test Meet 5")
            .leagueId("test")
            .athleticId("AID5")
            .hasResults(false)
            .locked(false)
            .savedContracts(new ArrayList<>())
            .date(Instant.now())
            .build();

    public static Meet testMeet6 = Meet.builder()
            .id("MT6")
            .name("Test Meet 6")
            .leagueId("test")
            .athleticId("AID6")
            .hasResults(false)
            .locked(false)
            .enabled(false)
            .savedContracts(new ArrayList<>())
            .date(Instant.now())
            .build();


}
