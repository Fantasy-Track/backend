package usecase.leagueCreation;

import com.google.inject.Inject;
import domain.entity.ProjectionReport;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import usecase.pageExtraction.ResultsPageExtractor;
import usecase.scoring.ResultsScorer;

import java.util.ArrayList;
import java.util.List;

public class GenerateLeagueRankings {

    private ResultsPageExtractor pageExtractor;

    @Inject
    public GenerateLeagueRankings(ResultsPageExtractor pageExtractor) {
        this.pageExtractor = pageExtractor;
    }

    public List<ProjectionReport> generateRankingsReport(String eventId, String divisionId) throws Exception {
        Elements rankings = pageExtractor.extractEventRankingsInDivision(eventId, divisionId);
        List<ProjectionReport> reports = new ArrayList<>();

        int rank = 1;
        for (Element e : rankings) {
            String rankStr = e.selectFirst("td").text();
            if (!rankStr.isEmpty()) {
                rank = Integer.parseInt(rankStr.substring(0, rankStr.length() - 1));
            }
            String athleteUrl = e.selectFirst("a[href*=/Athlete.aspx]").attr("href");
            reports.add(ProjectionReport.builder()
                    .athleteId(athleteUrl.substring(athleteUrl.lastIndexOf("=") + 1))
                    .eventId(eventId)
                    .projection(ResultsScorer.calcPoints(rank, rankings.size()))
                    .build());
        }
        return reports;
    }

}
