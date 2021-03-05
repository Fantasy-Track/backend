package domain.entity;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

public class EventResultsTable {

    private final Element resultsTable;

    public EventResultsTable(Element resultsTable) {
        this.resultsTable = resultsTable;
    }

    public String getEventID() {
        return resultsTable.id().split("_")[1] + (resultsTable.classNames().contains("relaySplit") ? "rs" : "");
    }

    public static String getEventIdFromTable(Element resultsTable) {
        return resultsTable.id().split("_")[1] + (resultsTable.classNames().contains("relaySplit") ? "rs" : "");
    }

    public List<ResultRow> getResultRows() {
        return resultsTable.select("tr[data-result-id]").select("[id~=^A.[0-9]]").stream().map(ResultRow::new).collect(Collectors.toList());
    }

}
