package usecase.scoring;

import domain.entity.IndexedResult;
import domain.entity.Mark;
import domain.entity.RankedResult;
import domain.entity.ScoredResult;

import java.util.ArrayList;
import java.util.List;

public class ResultsBank {

    public static List<IndexedResult> fieldResults = new ArrayList<>(
            List.of(
                    IndexedResult.builder()
                            .mark(new Mark("12-00", "9"))
                            .athleteId("A1")
                            .eventId("9")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("9-03.10", "9"))
                            .athleteId("A2")
                            .eventId("9")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("20-01.25", "9"))
                            .athleteId("A3")
                            .eventId("9")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("20' 01.25\"", "9"))
                            .athleteId("A4")
                            .eventId("9")
                            .build()
            ));

    public static List<IndexedResult> sprintEvents = new ArrayList<>(
            List.of(
                    IndexedResult.builder()
                            .mark(new Mark("12.00", "1"))
                            .athleteId("A1")
                            .eventId("1")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("9.10", "1"))
                            .athleteId("A2")
                            .eventId("1")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("10.65", "1"))
                            .athleteId("A3")
                            .eventId("1")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("10.65", "1"))
                            .athleteId("A4")
                            .eventId("1")
                            .build()
            ));

    public static List<IndexedResult> indexedResults = new ArrayList<>(
            List.of(
                    IndexedResult.builder()
                            .mark(new Mark("12.00", "1"))
                            .athleteId("A1")
                            .eventId("1")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("11.00", "1"))
                            .athleteId("A2")
                            .eventId("1")
                            .build(),
                    IndexedResult.builder()
                            .mark(new Mark("10.00", "1"))
                            .athleteId("A3")
                            .eventId("1")
                            .build()
            ));

    public static ScoredResult result1 = new ScoredResult(
            40,
            new RankedResult(
                    1,
                    IndexedResult.builder()
                            .athleteId("A1")
                            .eventId("18")
                            .build()));

    public static ScoredResult result2 = new ScoredResult(
            80,
            new RankedResult(
                    2,
                    IndexedResult.builder()
                            .athleteId("A1")
                            .eventId("9")
                            .build()));
    public static ScoredResult result3 = new ScoredResult(
            80,
            new RankedResult(
                    2,
                    IndexedResult.builder()
                            .athleteId("A2")
                            .eventId("9")
                            .build()));
    public static ScoredResult result4 = new ScoredResult(
            80,
            new RankedResult(
                    2,
                    IndexedResult.builder()
                            .athleteId("A3")
                            .eventId("9")
                            .build()));
}
