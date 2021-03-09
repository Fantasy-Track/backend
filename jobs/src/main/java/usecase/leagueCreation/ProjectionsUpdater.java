package usecase.leagueCreation;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.entity.ProjectionReport;
import domain.repository.AthleteUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectionsUpdater {

    private Logger logger = LoggerFactory.getLogger(ProjectionsUpdater.class);

    private GenerateLeagueRankings leagueRankings;
    private AthleteUpdateRepository athleteUpdateRepository;

    static final String SEASON_YEAR = System.getenv("SEASON_YEAR");
    static final String PROJECTIONS_YEAR_1 = String.valueOf(Integer.parseInt(SEASON_YEAR) - 1);
    static final String PROJECTIONS_YEAR_2 = String.valueOf(Integer.parseInt(SEASON_YEAR) - 2);

    @Inject
    public ProjectionsUpdater(GenerateLeagueRankings leagueRankings, AthleteUpdateRepository athleteUpdateRepository) {
        this.leagueRankings = leagueRankings;
        this.athleteUpdateRepository = athleteUpdateRepository;
    }

    public void updateProjections(String divisionId) throws Exception {
        long lastTime = System.currentTimeMillis();
        logger.info("Updating projections for division: " + divisionId);
        for (Events.Event event : Events.EVENTS) {
            if (event.maleId != null) updateProjections(event.maleId, divisionId);
            if (event.femaleId != null) updateProjections(event.femaleId, divisionId);
        }
        logger.info("Completed updating projections for division: " + divisionId + ". Time taken ms: " + (System.currentTimeMillis() - lastTime));
    }

    private void updateProjections(String eventId, String divisionId) throws Exception {
        List<ProjectionReport> reports = new ArrayList<>();
        reports.addAll(leagueRankings.generateRankingsReport(eventId, divisionId, PROJECTIONS_YEAR_1));
        reports.addAll(leagueRankings.generateRankingsReport(eventId, divisionId, PROJECTIONS_YEAR_2));

        Map<String, Athlete> athletes = getAthleteMap(reports);
        logger.info("Got " + reports.size() + " projections in event: " + eventId);
        for (ProjectionReport report : reports) {
            updateProjection(eventId, athletes, report);
        }
    }

    private void updateProjection(String eventId, Map<String, Athlete> athletes, ProjectionReport report) {
        logger.debug("Updating projection: " + report);
        String categoryId = Events.eventToCategory(eventId);
        if (!athletes.containsKey(report.athleteId)) return;
        Double categoryProjection = athletes.get(report.athleteId).projections.getOrDefault(categoryId, 0.0);
        if (categoryProjection > report.projection) return;
        athleteUpdateRepository.setCategoryProjection(report.athleteId, categoryId, report.projection);
    }

    private Map<String, Athlete> getAthleteMap(List<ProjectionReport> reports) {
        List<String> athleteIds = reports.stream()
                .map(projectionReport -> projectionReport.athleteId)
                .collect(Collectors.toList());
        return athleteUpdateRepository.getAthletesByIds(athleteIds).stream()
                .collect(Collectors.toMap(a -> a.id, a -> a));
    }

}
