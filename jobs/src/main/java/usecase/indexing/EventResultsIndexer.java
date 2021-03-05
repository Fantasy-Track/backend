package usecase.indexing;

import fantasyapp.repository.Events;
import domain.entity.*;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class EventResultsIndexer {

    private final EventResultsTable eventResultsTable;
    private final Meet meet;

    public EventResultsIndexer(EventResultsTable eventResultsTable, Meet meet) {
        this.eventResultsTable = eventResultsTable;
        this.meet = meet;
        assert Events.isValidEvent(eventResultsTable.getEventID()) : "That event cannot be processed yet";
    }

    public List<IndexedResult> indexEventResults() {
        List<IndexedResult> results = new ArrayList<>();
        for (ResultRow row : eventResultsTable.getResultRows()) {
            results.add(indexResult(row));
        }
        return results;
    }

    @SneakyThrows
    private IndexedResult indexResult(ResultRow row) {
        return IndexedResult.builder()
                .url(row.getResultUrl())
                .athleteId(row.getAthleteId())
                .leagueId(meet.leagueId)
                .meetId(meet.id)
                .eventId(eventResultsTable.getEventID())
                .mark(new Mark(row.getMark(), eventResultsTable.getEventID()))
                .date(meet.date)
                .build();
    }

}
