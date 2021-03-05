package mock;

import domain.entity.Result;
import domain.repository.ResultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockResultRepo implements ResultRepository {

    private ArrayList<Result> results = new ArrayList<>();

    public MockResultRepo() {
        results.add(Result.builder()
                .athleteId("A1")
                .teamId("T1")
                .eventId("9")
                .fantasyPoints(98.0)
                .mark("11.90")
                .id("R1")
                .meetId("M1")
                .rank(2)
                .build());
        results.add(Result.builder()
                .athleteId("A2")
                .teamId("T1")
                .eventId("8")
                .fantasyPoints(96.0)
                .mark("11.90")
                .id("R2")
                .meetId("M2")
                .rank(3)
                .build());
        results.add(Result.builder()
                .athleteId("A1")
                .teamId("T1")
                .eventId("6")
                .fantasyPoints(97.0)
                .mark("11.90")
                .id("R4")
                .meetId("M3")
                .rank(2)
                .build());
        results.add(Result.builder()
                .athleteId("A1")
                .teamId("T2")
                .eventId("6")
                .fantasyPoints(97.0)
                .mark("11.90")
                .id("R3")
                .meetId("M1")
                .rank(2)
                .build());
    }

    @Override
    public List<Result> getTeamResultsAtMeet(String teamId, String meetId) {
        return results.stream().filter(result -> teamId.equals(result.teamId) && meetId.equals(result.meetId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Result> getAthleteResultsInLeague(String athleteId, String league) {
        return results.stream().filter(result -> athleteId.equals(result.athleteId)).collect(Collectors.toList());
    }

    @Override
    public List<Result> getResultsAtMeet(String meetId) {
        return null;
    }
}
